from mcresources import ResourceManager, utils, loot_tables
from constants import *
from enum import Enum, auto

class Category(Enum):
    fruit = auto()
    vegetable = auto()
    grain = auto()
    bread = auto()
    dairy = auto()
    meat = auto()
    cooked_meat = auto()
    other = auto()


def generate(rm: ResourceManager):

    ### LOOT ###

    rm.block_loot('minecraft:netherrack', loot_tables.alternatives({'name': 'minecraft:netherrack', 'conditions': [{'condition': 'tfc:is_isolated'}]}, '2-4 beneath:nether_pebble'))
    rm.block_loot('minecraft:basalt', loot_tables.alternatives({'name': 'minecraft:basalt', 'conditions': [{'condition': 'tfc:is_isolated'}]}, '2-4 tfc:rock/loose/basalt'))
    rm.block_loot('minecraft:gilded_blackstone', 'minecraft:blackstone', {'name': 'beneath:gold_chunk', 'conditions': [loot_tables.random_chance(0.25)]})
    rm.block_loot('minecraft:gold_block', '3-6 beneath:gold_chunk')
    rm.block_loot('minecraft:blackstone', loot_tables.alternatives({'name': 'minecraft:blackstone', 'conditions': [{'condition': 'tfc:is_isolated'}]}, '2-4 beneath:blackstone_pebble'))
    rm.block_loot('minecraft:crimson_roots', loot_tables.alternatives({'name': 'minecraft:crimson_roots', 'conditions': [loot_tables.match_tag('forge:shears')]}, {'name': 'beneath:crimson_straw', 'conditions': [loot_tables.match_tag('tfc:sharp_tools')]}, {'name': 'beneath:seeds/crimson_roots', 'conditions': [loot_tables.random_chance(0.1)]}))
    rm.block_loot('minecraft:warped_roots', loot_tables.alternatives({'name': 'minecraft:warped_roots', 'conditions': [loot_tables.match_tag('forge:shears')]}, {'name': 'beneath:warped_straw', 'conditions': [loot_tables.match_tag('tfc:sharp_tools')]}, {'name': 'beneath:seeds/warped_roots', 'conditions': [loot_tables.random_chance(0.1)]}))
    rm.block_loot('minecraft:bone_block', '1-3 minecraft:bone_meal')
    rm.block_loot('minecraft:gravel', {
           'conditions': [loot_tables.silk_touch()],
           'name': 'tfc:rock/gravel/basalt'
       }, loot_tables.alternatives({
            'type': 'minecraft:item',
            'conditions': [loot_tables.fortune_table((0.1, 0.14285715, 0.25, 1.0))],
            'name': 'minecraft:flint'
       }, {
        'type': 'minecraft:item',
        'name': 'tfc:rock/gravel/basalt'
       }, conditions=['minecraft:survives_explosion']
    ))
    rm.block_loot('minecraft:nether_wart', '1-3 beneath:seeds/nether_wart', {'name': 'minecraft:nether_wart', 'conditions': [loot_tables.block_state_property('minecraft:nether_wart[age=3]')]})

    rm.loot('minecraft:ruined_portal', {
        'rolls': uniform(1, 2),
        'entries': [
            weight('minecraft:obsidian', 40, 1, 2),
            weight('minecraft:flint', 40, 1, 4),
            weight('tfc:metal/ingot/wrought_iron', 40, 1, 4),
            weight('minecraft:flint_and_steel', 40),
            weight('minecraft:fire_charge', 40),
            weight('beneath:gold_chunk', 15, 1, 6),
            weight('minecraft:bow', 15),
            weight('tfc:metal/sword/black_bronze', 15),
            weight('tfc:metal/axe/black_bronze', 15),
            weight('tfc:metal/shield/black_bronze', 15),
            weight('tfc:metal/scythe/black_bronze', 15),
            weight('tfc:metal/shovel/black_bronze', 15),
            weight('tfc:metal/helmet/black_bronze', 15),
            weight('tfc:metal/chestplate/black_bronze', 15),
            weight('tfc:metal/boots/black_bronze', 15),
            weight('tfc:metal/greaves/black_bronze', 15),
            weight('tfc:metal/horse_armor/black_bronze', 3),
            weight('minecraft:clock', 5),
            weight('tfc:metal/ingot/gold', 5, 2, 8),
            weight('minecraft:bell', 1),
            weight('tfc:bronze_bell', 1),
        ]
    }, path='chests', loot_type='minecraft:chest')

    rm.loot('minecraft:nether_bridge', {
        'rolls': uniform(2, 4),
        'entries': [
            weight('tfc:gem/diamond', 5, 1, 3),
            weight('tfc:metal/ingot/wrought_iron', 5, 1, 5),
            weight('tfc:metal/ingot/gold', 5, 1, 15),
            weight('tfc:metal/sword/wrought_iron', 5),
            weight('tfc:metal/chestplate/wrought_iron', 5),
            weight('minecraft:flint_and_steel', 5),
            weight('minecraft:saddle', 10),
            weight('tfc:metal/horse_armor/bismuth_bronze', 8),
            weight('tfc:metal/horse_armor/copper', 5),
            weight('minecraft:obsidian', 5, 2, 4),
            weight('beneath:pure_sorrow', 1),
            weight('beneath:pure_flame', 1),
            weight('beneath:pure_death', 1),
            weight('beneath:pure_destruction', 1),
            weight('beneath:pure_decay', 1),
            weight('beneath:seeds/ghost_pepper', 1, 3, 6),
            weight('beneath:seeds/gleamflower', 1, 3, 6),
        ]
    }, path='chests', loot_type='minecraft:chest')
    rm.loot('minecraft:bastion_treasure', {
        'rolls': 3,
        'entries': [
            weight('tfc:metal/chestplate/black_steel', 6),
            weight('tfc:metal/helmet/black_steel', 6),
            weight('tfc:metal/greaves/black_steel', 6),
            weight('tfc:metal/boots/black_steel', 6),
            weight('tfc:metal/sword/black_steel', 7),
            weight('tfc:metal/scythe/black_steel', 7),
            weight('tfc:gem/diamond', 10),
            weight('tfc:gem/opal', 10),
            weight('tfc:gem/ruby', 10),
            weight('beneath:pure_sorrow', 1),
            weight('beneath:pure_flame', 1),
            weight('beneath:pure_death', 1),
            weight('beneath:pure_destruction', 1),
            weight('beneath:pure_decay', 1),
        ]
    }, {
        'rolls': uniform(3, 4),
        'entries': [
            weight('minecraft:spectral_arrow', 1, 12, 25),
            weight('tfc:metal/ingot/pig_iron', 1, 2, 5),
            weight('tfc:metal/ingot/gold', 1, 3, 9),
            weight('minecraft:crying_obsidian', 1, 3, 5),
            weight('minecraft:gilded_blackstone', 1, 5, 15),
            weight('minecraft:magma_cream', 1, 3, 8),
        ]
    }, loot_type='minecraft:chest', path='chests')

    rm.loot('minecraft:bastion_other', {
        'entries': [
            weight('tfc:metal/pickaxe/black_bronze', 6, damage=True),
            weight('tfc:metal/shovel/black_bronze', 6, damage=True),
            weight('minecraft:crossbow', 6),
            weight('minecraft:spectral_arrow', 10, 10, 22),
            weight('minecraft:piglin_banner_pattern', 9),
            weight('minecraft:music_disc_pigstep', 5),
            weight('beneath:seeds/ghost_pepper', 1, 3, 6),
            weight('beneath:seeds/gleamflower', 1, 3, 6),
        ]
    }, {
        'entries': [
            weight('tfc:stone/axe/igneous_extrusive', 2, damage=True),
            weight('tfc:stone/shovel/igneous_extrusive', 2, damage=True),
            weight('tfc:stone/hoe/igneous_extrusive', 2, damage=True),
            weight('tfc:metal/scythe/copper', 2, damage=True),
            weight('minecraft:book', 10),
            weight('tfc:metal/ingot/gold', 10, 2, 4),
            weight('minecraft:gilded_blackstone', 1, 1, 5),
            weight('tfc:metal/chain/black_bronze', 1, 2, 10),
            weight('minecraft:obsidian', 1, 4, 6),
            weight('minecraft:arrow', 2, 5, 17),
            weight('tfc:food/cooked_pork', 1),
        ]
    }, loot_type='minecraft:chest', path='chests')

    rm.loot('minecraft:bastion_bridge', {
        'entries': [
            weight('minecraft:crossbow', 1, damage=True),
            weight('minecraft:spectral_arrow', 1, 10, 28, damage=True),
            weight('minecraft:gilded_blackstone', 1, 8, 12),
            weight('minecraft:crying_obsidian', 1, 3, 8),
            weight('tfc:metal/ingot/gold', 1, 4, 9),
            weight('tfc:metal/ingot/pig_iron', 1, 4, 9),
            weight('tfc:metal/chestplate/steel', 1),
            weight('tfc:metal/helmet/steel', 1),
            weight('tfc:metal/greaves/steel', 1),
            weight('tfc:metal/boots/steel', 1),
            weight('beneath:pure_sorrow', 1),
            weight('beneath:pure_flame', 1),
            weight('beneath:pure_death', 1),
            weight('beneath:pure_destruction', 1),
            weight('beneath:pure_decay', 1),
        ]
    }, {
        'rolls': uniform(2, 4),
        'entries': [
            weight('minecraft:string', 1, 4, 16),
            weight('minecraft:leather', 1, 4, 6),
            weight('minecraft:arrow', 1, 5, 17),
            weight('tfc:glow_arrow', 1, 5, 17),
        ]
    }, loot_type='minecraft:chest', path='chests')

    rm.loot('minecraft:bastion_hoglin_stable', {
        'entries': [
            weight('tfc:metal/shovel/wrought_iron', 15, damage=True),
            weight('tfc:metal/pickaxe/wrought_iron', 15, damage=True),
            weight('minecraft:saddle', 12),
        ]
    }, {
        'rolls': uniform(3, 4),
        'entries': [
            weight('tfc:metal/axe/copper', 1, damage=True),
            weight('minecraft:crying_obsidian', 1, 1, 5),
            weight('minecraft:glowstone', 1, 3, 6),
            weight('tfc:metal/lamp/bronze', 1),
            weight('tfc:metal/lamp/copper', 1),
            weight('minecraft:crimson_nylium', 1, 2, 7),
            weight('minecraft:warped_nylium', 1, 2, 7),
            weight('minecraft:leather', 2, 2, 7),
            weight('minecraft:arrow', 1, 5, 17),
            weight('tfc:glow_arrow', 1, 5, 17),
            weight('minecraft:string', 1, 3, 8),
            weight('beneath:wood/sapling/crimson', 1, 2, 7),
            weight('beneath:wood/sapling/warped', 1, 2, 7),
        ]
    }, loot_type='minecraft:chest', path='chests')

    rm.loot('minecraft:piglin_bartering', {
        'entries': [
            weight('minecraft:ender_pearl', 1, 2, 4),
            weight('minecraft:string', 2, 3, 9),
            weight('tfc:burlap_cloth', 1, 2, 4),
            weight('minecraft:quartz', 2, 5, 12),
            weight('minecraft:obsidian', 1),
            weight('minecraft:crying_obsidian', 1, 1, 3),
            weight('minecraft:leather', 2, 2, 4),
            weight('minecraft:saddle', 1),
            weight('minecraft:nether_brick', 2, 8, 12),
            weight('minecraft:arrow', 2, 6, 12),
            weight('minecraft:spectral_arrow', 2, 6, 12),
            weight('tfc:glow_arrow', 2, 6, 12),
            weight('tfc:rock/gravel/basalt', 1, 8, 16),
            weight('minecraft:blackstone', 1, 8, 16),
            weight('tfc:powder/flux', 2, 4, 12)
        ]
    }, loot_type='minecraft:barter', path='gameplay')

    rm.entity_loot('minecraft:hoglin', '2-4 tfc:food/pork', '3-6 minecraft:bone', 'beneath:cursed_hide')
    rm.entity_loot('minecraft:piglin', '1-2 tfc:food/pork', '1-3 minecraft:bone')
    rm.entity_loot('minecraft:strider', '1-2 minecraft:bone', 'beneath:cursed_hide')
    rm.entity_loot('minecraft:zoglin', '1-3 minecraft:bone', '1-3 minecraft:rotten_flesh', 'beneath:cursed_hide')
    rm.entity_loot('minecraft:zombie_piglin', '1-3 minecraft:bone', '1-3 minecraft:rotten_flesh')

    ### BLOCK TAGS ###
    rm.block_tag('breaks_slowly', 'minecraft:netherrack', 'minecraft:soul_sand', 'minecraft:soul_soil', 'minecraft:magma_block', 'minecraft:warped_nylium', 'minecraft:crimson_nylium')

    rm.block_tag('tfc:breaks_when_isolated', 'minecraft:basalt', 'minecraft:blackstone', 'minecraft:netherrack', 'beneath:crackrack')
    rm.block_tag('tfc:tree_grows_on', 'minecraft:netherrack', 'minecraft:warped_nylium', 'minecraft:crimson_nylium')
    rm.block_tag('beneath:hellforge_insulation', 'beneath:hellbricks')
    rm.block_tag('beneath:nether_bush_plantable_on', '#minecraft:base_stone_nether', 'minecraft:soul_soil', 'minecraft:soul_sand', 'beneath:soul_farmland', 'minecraft:crimson_nylium', 'minecraft:warped_nylium')
    rm.block_tag('minecraft:mineable/pickaxe', 'beneath:blackstone_aqueduct')
    rm.block_tag('minecraft:mineable/shovel', 'beneath:cursecoal_pile', 'beneath:hellforge')

    for shroom in MUSHROOMS:
        rm.block_tag('mushrooms', 'beneath:mushroom/%s' % shroom)
        if shroom in POISONOUS_MUSHROOMS:
            rm.block_tag('poisonous_mushrooms', 'beneath:mushroom/%s' % shroom)

    ### ITEM TAGS ###
    rm.item_tag('sparks_on_sulfur', *['#tfc:metal_item/%s' % metal for metal in ('black_steel', 'blue_steel', 'red_steel', 'steel', 'wrought_iron', 'cast_iron')])
    rm.item_tag('usable_in_juicer', '#tfc:foods/fruits', '#beneath:mushrooms')

    block_and_item_tag(rm, 'tfc:rock/aqueduct', 'beneath:blackstone_aqueduct')
    rm.item_tag('tfc:rock_knapping', 'beneath:nether_pebble', 'beneath:blackstone_pebble')
    rm.item_tag('tfc:metamorphic_rock', 'beneath:blackstone_pebble')
    rm.item_tag('tfc:sedimentary_rock', 'beneath:nether_pebble')
    block_and_item_tag(rm, 'minecraft:stone_bricks', 'beneath:hellbricks')

    for shroom in MUSHROOMS:
        rm.item_tag('mushrooms', 'beneath:food/%s' % shroom)
        if shroom in POISONOUS_MUSHROOMS:
            rm.item_tag('poisonous_mushrooms', 'beneath:food/%s' % shroom)

    ### ENTITY TAGS ###
    rm.entity_tag('can_be_sacrificed', 'tfc:pig', 'tfc:goat', 'tfc:sheep')

    ### ITEM HEATS ###
    metal_data = TFC_METALS['gold']
    item_heat(rm, 'gold_chunk', 'beneath:gold_chunk', metal_data.ingot_heat_capacity(), int(metal_data.melt_temperature), mb=40)

    ### FUELS ###
    fuel_item(rm, 'cursecoal', 'beneath:cursecoal', 1800, 1350)
    for wood in WOODS:
        fuel_item(rm, wood + '_log', ['beneath:wood/log/' + wood, 'beneath:wood/wood/' + wood, 'beneath:wood/stripped_wood/' + wood, 'beneath:wood/stripped_log/' + wood], 800, 1000, 0.6)

    ### FOODS ###
    food_item(rm, 'ghost_pepper', 'beneath:ghost_pepper', Category.vegetable, 4, 1, 0, 2.5, veg=1)

    for shroom in MUSHROOMS:
        food_item(rm, shroom, 'beneath:food/%s' % shroom, Category.vegetable, 4, 1, 2, 1.5, veg=1)

    ### FERTILIZERS ###
    n_fertilizer(rm, 'pure_death', 'beneath:pure_death', death=0.1)
    n_fertilizer(rm, 'pure_decay', 'beneath:pure_decay', decay=0.1)
    n_fertilizer(rm, 'pure_destruction', 'beneath:pure_destruction', destr=0.1)
    n_fertilizer(rm, 'pure_sorrow', 'beneath:pure_sorrow', sorrow=0.1)
    n_fertilizer(rm, 'pure_flame', 'beneath:pure_flame', flame=0.1)

    n_fertilizer(rm, 'sulfur', 'tfc:powder/sulfur', decay=0.2, flame=0.1)
    n_fertilizer(rm, 'gunpowder', 'minecraft:gunpowder', destr=0.6, flame=0.1, death=0.4)
    n_fertilizer(rm, 'ghast_tear', 'minecraft:ghast_tear', sorrow=0.3)
    n_fertilizer(rm, 'blaze_powder', 'minecraft:blaze_powder', flame=0.2)
    n_fertilizer(rm, 'agonizing_fertilizer', 'beneath:agonizing_fertilizer', flame=0.1, death=0.1, decay=0.1, destr=0.1, sorrow=0.1)

    ### DAMAGE TYPES ###
    damage_type(rm, 'sulfur')


def damage_type(rm: ResourceManager, name_parts: utils.ResourceIdentifier, message_id: str = None, exhaustion: float = 0.0, scaling: str = 'when_caused_by_living_non_player', effects: str = None, message_type: str = None):
    rm.data(('damage_type', name_parts), {
        'message_id': message_id if message_id is not None else 'beneath.' + name_parts,
        'exhaustion': exhaustion,
        'scaling': scaling,
        'effects': effects,
        'death_message_type': message_type
    })

def n_fertilizer(rm: ResourceManager, name: str, ingredient: str, death: float = None, destr: float = None, flame: float = None, decay: float = None, sorrow: float = None):
    rm.data(('beneath', 'nether_fertilizers', name), {
        'ingredient': utils.ingredient(ingredient),
        'death': death,
        'destruction': destr,
        'flame': flame,
        'decay': decay,
        'sorrow': sorrow
    })

def item_heat(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, heat_capacity: float, melt_temperature: Optional[float] = None, mb: Optional[int] = None):
    if melt_temperature is not None:
        forging_temperature = round(melt_temperature * 0.6)
        welding_temperature = round(melt_temperature * 0.8)
    else:
        forging_temperature = welding_temperature = None
    if mb is not None:
        # Interpret heat capacity as a specific heat capacity - so we need to scale by the mB present. Baseline is 100 mB (an ingot)
        # Higher mB = higher heat capacity = heats and cools slower = consumes proportionally more fuel
        heat_capacity = round(10 * heat_capacity * mb) / 1000
    rm.data(('tfc', 'item_heats', name_parts), {
        'ingredient': utils.ingredient(ingredient),
        'heat_capacity': heat_capacity,
        'forging_temperature': forging_temperature,
        'welding_temperature': welding_temperature
    })

def fuel_item(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, duration: int, temperature: float, purity: float = None):
    rm.data(('tfc', 'fuels', name_parts), {
        'ingredient': utils.ingredient(ingredient),
        'duration': duration,
        'temperature': temperature,
        'purity': purity,
    })

def food_item(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, category: Category, hunger: int, saturation: float, water: int, decay: float, fruit: Optional[float] = None, veg: Optional[float] = None, protein: Optional[float] = None, grain: Optional[float] = None, dairy: Optional[float] = None):
    rm.item_tag('tfc:foods', ingredient)
    rm.data(('tfc', 'food_items', name_parts), {
        'ingredient': utils.ingredient(ingredient),
        'hunger': hunger,
        'saturation': saturation,
        'water': water if water != 0 else None,
        'decay_modifier': decay,
        'fruit': fruit,
        'vegetables': veg,
        'protein': protein,
        'grain': grain,
        'dairy': dairy
    })
    rm.item_tag('tfc:foods', ingredient)
    if category in (Category.fruit, Category.vegetable):
        rm.item_tag('tfc:foods/%ss' % category.name.lower(), ingredient)
    if category in (Category.meat, Category.cooked_meat):
        rm.item_tag('tfc:foods/meats', ingredient)
        if category == Category.cooked_meat:
            rm.item_tag('tfc:foods/cooked_meats', ingredient)
        else:
            rm.item_tag('tfc:foods/raw_meats', ingredient)
    if category == Category.dairy:
        rm.item_tag('tfc:foods/dairy', ingredient)


def block_and_item_tag(rm: ResourceManager, name_parts: utils.ResourceIdentifier, *values: utils.ResourceIdentifier, replace: bool = False):
    rm.block_tag(name_parts, *values, replace=replace)
    rm.item_tag(name_parts, *values, replace=replace)

def weight(name: str, weight_amount: int, minim: int = None, maxim: int = None, damage: bool = False):
    cfg = {
        'name': name,
        'weight': weight_amount,
        'functions': []
    }
    if minim is not None and maxim is not None:
        cfg['functions'].append(loot_tables.set_count(minim, maxim))
    if damage:
        cfg['functions'].append(set_damage(0.1, 0.9))
    if len(cfg['functions']) == 0:
        cfg['functions'] = None
    return cfg

def set_damage(minim: float, maxim: float):
    return {
        'function': 'minecraft:set_damage',
        'damage': uniform(minim, maxim)
    }

def uniform(minim: float, maxim: float):
    return {
        'type': 'minecraft:uniform',
        'min': minim,
        'max': maxim
    }
