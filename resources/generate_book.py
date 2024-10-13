import format_lang
from patchouli import *
from argparse import ArgumentParser
from typing import Optional

BOOK_LANGUAGES = ('en_us',)
MOD_LANGUAGES = ('en_us',)

class LocalInstance:
    INSTANCE_DIR = None

    @staticmethod
    def wrap(rm: ResourceManager):
        def data(name_parts: ResourceIdentifier, data_in: JsonObject, root_domain: str = 'data'):
            return rm.write((LocalInstance.INSTANCE_DIR, '/'.join(utils.str_path(name_parts))), data_in)

        if LocalInstance.INSTANCE_DIR is not None:
            rm.data = data
            return rm
        return None

def main_with_args():
    parser = ArgumentParser('generate_book.py')
    parser.add_argument('--translate', type=str, default='en_us', help='The language to translate to')
    parser.add_argument('--local', type=str, default=None, help='The directory of a local .minecraft to copy into')
    parser.add_argument('--translate-all', type=str, default=None, help='If all languages should be translated')
    parser.add_argument('--format', type=str, default=None, help='Format the mod languages')
    parser.add_argument('--reverse-translate', type=str, default=None, help='Reverse a translation from the mod files.')

    args = parser.parse_args()

    if args.format:
        do_format()
        return

    if args.translate_all:
        do_format()
        for la in BOOK_LANGUAGES:
            main(la, args.local, False, reverse_translate=args.reverse_translate is not None)
    else:
        main(args.translate, args.local, False, reverse_translate=args.reverse_translate is not None)

def do_format():
    # format_lang.main(False, 'minecraft', BOOK_LANGUAGES)
    format_lang.main(False, 'beneath', MOD_LANGUAGES)

def main(translate_lang: str, local_minecraft_dir: Optional[str], validate: bool, validating_rm: ResourceManager = None, reverse_translate: bool = False):
    LocalInstance.INSTANCE_DIR = local_minecraft_dir

    rm = ResourceManager('tfc', './src/main/resources')
    if validate:
        rm = validating_rm
    i18n = I18n(translate_lang, validate)

    print('Writing book at %s' % translate_lang)
    make_book(rm, i18n, local_instance=False, reverse_translate=reverse_translate)

    i18n.flush()

    if LocalInstance.wrap(rm):
        print('Copying %s book into local instance at: %s' % (translate_lang, LocalInstance.INSTANCE_DIR))
        make_book(rm, I18n(translate_lang, validate), local_instance=True)

def make_book(rm: ResourceManager, i18n: I18n, local_instance: bool = False, reverse_translate: bool = False):
    book = Book(rm, 'field_guide', {}, i18n, local_instance, reverse_translate)

    book.category('beneath', 'Beneath', 'All about what lies Beneath', 'beneath:ore/blackstone_sylvite', is_sorted=False, entries=(
        entry('beneath', 'What Lies Beneath', 'beneath:warped_thatch', pages=(
            text('For years, the tranquility of the overworld was all you knew. It was difficult, yes, but the sun came out; the birds chirped in the morning; and there was not a bit of $(thing)magic$(). One day, you decided that this was not enough. You dreamed of a cursed world, with strange creatures, danger beyond imagination, and unfathomable riches to exploit. That world is Beneath you.'),
            text('The world of Beneath is a lot like the underworld that you are familiar with, but in the style of TerraFirmaCraft. Trees grow there, and you will find rocks on the ground that you can make into tools. Some things have been removed, some are replaced, and plenty of new content has been added. One thing is for certain: you must work harder to survive.'),
        )),
        entry('ores_and_minerals', 'Ores and Minerals', 'beneath:ore/normal_nether_gold', pages=(
            text('Beneath the overworld, you will still find ore veins to exploit. It is important to note that $(thing)Netherrack$() does not collapse, like raw stone would. However, $(thing)Blackstone$(), $(thing)Nether Bricks$(), $(thing)Cobblerack$() and miscellaneous other blocks do. Prospecting will still work Beneath the overworld.'),
            block_spotlight('Nether Gold', 'Nether Gold replaces netherrack, and is common. Beneath lava, there is more gold, and richer.', 'beneath:ore/normal_nether_gold').anchor('nether_gold'),
            block_spotlight('Cursecoal', 'Cursecoal is a powerful $(l:beneath/hellforge)fuel$(). It replaces netherrack above level 80.', 'beneath:ore/nether_cursecoal').anchor('cursecoal'),
            block_spotlight('Sylvite', 'Sylvite replaces basalt at any level.', 'beneath:ore/blackstone_sylvite').anchor('sylvite'),
            block_spotlight('Nether Quartz', 'Nether Quartz replaces netherrack at all levels, and is common.', 'minecraft:nether_quartz_ore').anchor('quartz'),
            block_spotlight('Slimed Netherrack', 'Slimed Netherrack occasionally replaces Netherrack above lava level.', 'beneath:ore/slimed_netherrack').anchor('slimed_netherrack'),
        )),
        entry('curious_blocks', 'Curious Blocks', 'beneath:soul_clay', pages=(
            non_text_first_page(),
            block_spotlight('Soul Clay', 'Soul Clay may be discovered in soul sand valleys. It is a source of regular clay.', 'beneath:soul_clay'),
            block_spotlight('Sulfur', 'Sulfur can be broken with a shovel. Be careful, though: iron-containing items (including steel) explode in contact with sulfur!', 'beneath:sulfur'),
            block_spotlight('Thatch', 'Cutting grass will yield you warped or crimson straw. This can be made into thatch, the normal way.', 'beneath:warped_thatch'),
            crafting('beneath:crafting/blackstone_aqueduct', text_contents='The blackstone aqueduct allows lava to be conducted at any distance.'),
            crafting('beneath:crafting/hellbricks', text_contents='Hellbricks are a building block that is also used for the Hellforge.'),
            crafting('beneath:crafting/blackstone_brick', text_contents='Blackstone bricks are made like regular bricks, and that includes needing to create the brick item.'),
        )),
        entry('water', 'Water', 'beneath:juicer', pages=(
            text('Regrettably, water is still necessary when you are Beneath. Water stored in barrels will not evaporate when you are Beneath. However, there is also the option of crafting a $(thing)Juicer$().'),
            crafting('beneath:crafting/juicer', text_contents='The juicer is a drinking vessel that allows the compression of $(thing)Mushrooms$() into water. The single-slot user interface automatically processes inserted mushrooms into water.'),
            text('Mushrooms spawn freely around the place Beneath the overworld. Fresh fruits may also be squeezed into water, if you have them handy.'),
            empty_last_page(),
        )),
        entry('crops', 'Crops', 'beneath:gleamflower', pages=(
            text('Soul soil may be tilled to form $(thing)Soul Farmland$(). Crops work a little differently when you are Beneath. There are five nutrients: Death, Destruction, Decay, Sorrow, and Flame. Nutrients are viewed by holding a hoe, but will not appear unless they have a value greater than zero.'),
            text('Crops have a preferred nutrient: Instead of consuming it, this is the nutrient they will add to over time. The crop will happily consume the other four nutrients in order to grow. Nether crops cannot die, but still need block light to grow.'),
            text('The fertilizers are as follows: $(br)$(br)Sulfur: 20% decay, 10% flame$(br)Gunpowder: 60% Destruction, 10% Flame, 40% Death$(br)Ghast Tear: 30% Sorrow$(br)Blaze Powder: 20% Flame'),
            text('The fertilizers are also available in obtainable \'pure\' forms from chest loot. Crafting these together yields $(thing)Agonizing Fertilizer$(), which has them all in a neat package.'),
            text('Crimson Roots (Decay) and Warped Roots (Destruction) are growable as crops. These are most useful as means of improving the nutrient quality of your soul farmland. Nether Wart (Death), found in fortresses, can also be grown as a crop.'),
            block_spotlight('Gleamflower', 'The Gleamflower (Flame) can be grown as a crop, and also found in the world as a plant. It is useful as an infinite light source.', 'beneath:gleamflower'),
            item_spotlight('beneath:ghost_pepper', text_contents='Ghost Peppers (Sorrow) are an edible vegetable that can be grown in the Beneath!'),
            empty_last_page()
        )),
        entry('burpflower', 'Burpflowers', 'beneath:burpflower', pages=(
            text('The $(thing)Burpflower$() has the power to put a helpful curse on other blocks. It can be found all around the Beneath. The Burpflower has a facing direction, which is the direction that it performs the action in. Behind it, place a $(thing)Sulfur$() block to charge it.'),
            text('Soon, if there is sulfur, the burpflower will charge, and have a yellow sulfuric flower. It then soon will make a burping sound and expel sulfuric smoke. This smoke has the power to change blocks into other blocks.'),
            text('First, Nether Bricks can be changed into $(thing)Hellbricks$(). Charcoal piles can be transformed into $(l:beneath/ores_and_minerals#cursecoal)Cursecoal Piles$(). Further, $(thing)Decay$() can be added to $(thing)Soul Farmland$().'),
            empty_last_page(),
        )),
        entry('unposter', 'The Unposter', 'beneath:unposter', pages=(
            text('The $(thing)Unposter$() is used to grow mushrooms. Mushrooms, ghast tears, and nether crops can be deposited into it. When it is by a mushroom, it will consume the items inside to multiply that mushroom in the area around it, once a day.'),
            crafting('beneath:crafting/unposter'),
        )),
        entry('hellforge', 'The Hellforge', 'beneath:cursecoal', pages=(
            text('The $(thing)Hellforge$() is a gratuitously large forge, with high capacity for fuel, products, and heating. It is constructed from Hellbricks, $(l:beneath/ores_and_minerals#cursecoal)Cursecoal$(), and Blackstone Aqueducts.'),
            multiblock('Hellforge', '', pattern=(
                ('ZXXXZ', 'XYYYX', 'XY0YX', 'XYYYX', 'ZXXXZ'),
                ('     ', ' XXX ', ' XXX ', ' XXX ', '     ')
            ), mapping={'Z': 'beneath:blackstone_aqueduct[fluid=lava]', 'Y': 'beneath:hellforge_side', '0': 'beneath:hellforge', 'X': 'beneath:hellbricks',}),
            text('The Hellforge requires lava to flow in aqueducts in its four corners, as well as 21 hellbricks. The 3x3 center must be filled with $(l:beneath/ores_and_minerals#cursecoal)Cursecoal$() piles, which then may be lit to start the forge.'),
            text('The item slots of the Hellforge may contain fuel, or things to be smelted; there is no distinction. Be careful what you put in! There are also slots for melting things, as with the charcoal forge. The lava meter on the screen indicates temperature (as well as the visual movement of its items in world).')
        )),
        entry('how_to_go_beneath', 'How To Go Beneath', 'minecraft:flint_and_steel', pages=(
            text('The sacrifice needed to go Beneath is great. First, hold a $(thing)Scythe$(). Find a Pig, Goat, or Sheep. Then, obtain seven offerings. Valid offerings are skulls, anvils, placed gold, pig iron, or black steel ingots, and gold or black steel plated blocks.'),
            text('Kill the animal within a 5 block radius of the offerings. A portal will be created, but be warned! The Beneath will temporarily invade your world, wreaking havoc and causing confusion.'),
            text('For the more common types of sacrifices, that do not open portals to other realms, see the chapter on $(l:beneath/ancient_altar)Ancient Altars$().'),
            empty_last_page(),
        )),
        entry('ancient_altar', 'Ancient Altars', 'beneath:lost_page', pages=(
            text('There are more routine types of $(thing)Sacrifices$() that can be done when you are Beneath. These require the obtaining of $(thing)Lost Pages$(), fragments of forbidden knowledge left behind. These are found in structures like the $(thing)Fortress$(), $(thing)Ruined Portal$(), and $(thing)Bastion remnant$().'),
            crafting('beneath:crafting/ancient_altar', text_contents='The $(thing)Ancient Altar$() can hold and display up to a stack of any item. $(item)$(k:key.use)$() is used to insert or swap items in the altar.'),
            page_break(),
            text('If $(thing)Blackstone$() is not readily available for crafting an Ancient Altar, it may be crafted from $(thing)Soot$() and regular stone.'),
            crafting('beneath:crafting/blackstone_from_soot', 'beneath:crafting/blackstone_bricks_from_soot'),
            page_break(),
            text('Most sacrifices require a cut $(thing)Gem$() of any kind to activate, and consume the gem upon completion. The most basic sacrifice is that of reading the information on the Lost Page. To do this, place a blank page on the altar and $(item)$(k:key.use)$() with the gem. To open a page and read its contents, hold it and press $(item)$(k:key.use)$().'),
            text('Lost Pages have three items of interest: their $(thing)Cost$(), $(thing)Reward$(), and $(thing)Punishment$(). These values are fixed when the page is activated, but are randomized from a fixed set of values otherwise. This means that two pages which perform the same cost and reward may have different levels of utility.'),
            text('The $(thing)Cost$() of a sacrifice is a single ingredient, much like crafting, with an amount. This cost is to be placed on other ancient altars, placed on the same level within 5 blocks of the center altar, on which the lost page goes. Stacks containing items in excess of the total cost of the sacrifice may still be consumed, so be precise!'),
            text('The $(thing)Reward$() of a sacrifice is obtained by $(item)$(k:key.use)$() on the altar with the page, if the cost is present. The reward is spawned as items above the altar. $(thing)Rewards are reduced by half$() if performed in the Overworld.'),
            text('The $(thing)Punishment$() of a sacrifice can range from \'No Punishment\', to some extra items, to an attack from some dangerous monsters. The punishment is always known in advance, as it is written on the page. There is no detailed list of punishments, but feel free to experiment (also, it is not hard to figure out what the punishments are, if you know where to look).'),
            text('The list of sacrifices is detailed in the $(l:beneath/list_of_sacrifices)List of Sacrifices$() chapter for easy reading.'),
        )),
        entry('list_of_sacrifices', 'List of Sacrifices', 'tfc:gem/diamond', pages=(
            text('This entry details the possible costs and rewards for the different $(l:beneath/ancient_altar)Sacrifices$() one can make in Beneath.'),
            text('$(li)Raw Slime$() -> Rich Garnierite$()$(li)Cursed Hide -> Large Raw Hide$()$(li)Cobblestone -> Blackstone$()'),
        )),
    ))

    book.build()

# beneath Pages

def knapping(recipe: str, text_content: TranslatableStr) -> Page: return recipe_page('knapping_recipe', recipe, text_content)

def alloy_recipe(title: str, ingot: str, *components: Tuple[str, int, int], text_content: str) -> Page:
    recipe = ''.join(['$(li)%d - %d %% : $(thing)%s$()' % (lo, hi, alloy) for (alloy, lo, hi) in components])
    return item_spotlight(ingot, title, False, '$(br)$(bold)Requirements:$()$(br)' + recipe + '$(br2)' + text_content)

def custom_component(x: int, y: int, class_name: str, data: JsonObject) -> Component:
    return Component('patchouli:custom', x, y, {'class': 'com.eerussianguy.beneath.compat.patchouli.' + class_name, **data})


if __name__ == '__main__':
    main_with_args()

