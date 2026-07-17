from typing import NamedTuple, List, Optional, Tuple, Dict, Set, Literal


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()

class MetalItem(NamedTuple):
    type: Literal['ingot', 'part', 'all', 'weathering']
    parent_model: str | None
    mold: bool

class Metal(NamedTuple):
    type: Literal['ingot', 'part', 'all']
    weathering: bool

    def has_block(self, item: str) -> bool: return self.has(METAL_BLOCKS[item])

    def has(self, item: MetalItem) -> bool:
        if item.type == 'weathering':
            return self.weathering
        if item.type == 'all':
            return self.type == 'all'
        if item.type == 'part':
            return self.type in ('all', 'part')
        if item.type == 'ingot':
            return True

class Rock(NamedTuple):
    category: str
    sand: str

ROCK_CATEGORIES: List[str] = ['sedimentary', 'metamorphic', 'igneous_extrusive', 'igneous_intrusive']

METALS: dict[str, Metal] = {
    'bismuth': Metal('part', False),
    'bismuth_bronze': Metal('all', False),
    'black_bronze': Metal('all', False),
    'bronze': Metal('all', True),
    'brass': Metal('part', True),
    'copper': Metal('all', True),
    'gold': Metal('part', False),
    'nickel': Metal('part', False),
    'rose_gold': Metal('part', False),
    'silver': Metal('part', True),
    'tin': Metal('part', False),
    'zinc': Metal('part', False),
    'sterling_silver': Metal('part', True),
    'wrought_iron': Metal('all', True),
    'cast_iron': Metal('part', False),
    'pig_iron': Metal('ingot', False),
    'steel': Metal('all', True),
    'black_steel': Metal('all', False),
    'blue_steel': Metal('all', False),
    'red_steel': Metal('all', False),
    'weak_steel': Metal('ingot', False),
    'weak_blue_steel': Metal('ingot', False),
    'weak_red_steel': Metal('ingot', False),
    'high_carbon_steel': Metal('ingot', False),
    'high_carbon_black_steel': Metal('ingot', False),
    'high_carbon_blue_steel': Metal('ingot', False),
    'high_carbon_red_steel': Metal('ingot', False),
    'unknown': Metal('ingot', False)
}

METAL_BLOCKS: dict[str, MetalItem] = {
    'block': MetalItem('part', 'block/block', False),
    'exposed_block': MetalItem('weathering', 'block/block', False),
    'weathered_block': MetalItem('weathering', 'block/block', False),
    'oxidized_block': MetalItem('weathering', 'block/block', False),
    'block_slab': MetalItem('part', 'block/block', False),
    'exposed_block_slab': MetalItem('weathering', 'block/block', False),
    'weathered_block_slab': MetalItem('weathering', 'block/block', False),
    'oxidized_block_slab': MetalItem('weathering', 'block/block', False),
    'block_stairs': MetalItem('part', 'block/block', False),
    'exposed_block_stairs': MetalItem('weathering', 'block/block', False),
    'weathered_block_stairs': MetalItem('weathering', 'block/block', False),
    'oxidized_block_stairs': MetalItem('weathering', 'block/block', False),
    'grate': MetalItem('all', 'block/block', False),
    'exposed_grate': MetalItem('all', 'block/block', False),
    'weathered_grate': MetalItem('all', 'block/block', False),
    'oxidized_grate': MetalItem('all', 'block/block', False),
    'anvil': MetalItem('part', 'tfc:block/anvil', False),
    'bars': MetalItem('part', 'item/generated', False),
    'chain': MetalItem('part', 'tfc:block/chain', False),
    'lamp': MetalItem('part', 'tfc:block/lamp', False),
    'trapdoor': MetalItem('part', 'tfc:block/trapdoor', False),
}

ROCKS: Dict[str, Rock] = {
    'granite': Rock('igneous_intrusive', 'white'),
    'diorite': Rock('igneous_intrusive', 'white'),
    'gabbro': Rock('igneous_intrusive', 'black'),
    'shale': Rock('sedimentary', 'black'),
    'claystone': Rock('sedimentary', 'brown'),
    'limestone': Rock('sedimentary', 'white'),
    'conglomerate': Rock('sedimentary', 'green'),
    'dolomite': Rock('sedimentary', 'black'),
    'chert': Rock('sedimentary', 'yellow'),
    'chalk': Rock('sedimentary', 'white'),
    'rhyolite': Rock('igneous_extrusive', 'red'),
    'basalt': Rock('igneous_extrusive', 'red'),
    'andesite': Rock('igneous_extrusive', 'red'),
    'dacite': Rock('igneous_extrusive', 'yellow'),
    'quartzite': Rock('metamorphic', 'white'),
    'slate': Rock('metamorphic', 'yellow'),
    'phyllite': Rock('metamorphic', 'brown'),
    'schist': Rock('metamorphic', 'green'),
    'gneiss': Rock('metamorphic', 'green'),
    'marble': Rock('metamorphic', 'yellow')
}

WOODS = ['crimson', 'warped']

TREE_SAPLING_DROP_CHANCES = {
    'crimson': 0.0428,
    'warped': 0.0115,
}

NUTRIENTS = ('death', 'destruction', 'decay', 'sorrow', 'flame')

SIMPLE_ITEMS = ('gold_chunk', 'cursed_hide', 'cursecoal', 'pure_death', 'pure_decay', 'pure_destruction', 'pure_sorrow', 'pure_flame', 'agonizing_fertilizer', 'crimson_straw', 'warped_straw', 'ghost_pepper', 'blackstone_brick',
                'crackrack_rock', 'juicer', 'raw_slime', 'lost_page', 'tome',
                )
MUSHROOMS = ('button', 'chantrelle', 'death_cap', 'destroying_angels', 'fools_funnel', 'oyster', 'parasol', 'portobello', 'shiitake', 'sulfur_tuft')
POISONOUS_MUSHROOMS = ('death_cap', 'destroying_angels', 'fools_funnel', 'sulfur_tuft')

ROCK_SPIKE_PARTS = ('base', 'middle', 'tip')

SPAWN_EGG_ENTITIES = ('red_elk',)

DEFAULT_CROPS = {
    'crimson_roots': 4,
    'ghost_pepper': 7,
    'gleamflower': 4,
    'nether_wart': 4,
    'warped_roots': 4
}

DEFAULT_LANG = {
    'beneath.creative_tab.beneath': 'Beneath Items',
    'entity.beneath.leviathan_fireball': 'Leviathan Fireball',
    'entity.beneath.blaze_leviathan': 'Blaze Leviathan',
    'entity.beneath.red_elk': 'Red Elk',
    'tfc.recipe.barrel.beneath.barrel.mortar': 'Mortar',
    'beneath.nutrient.decay': 'Decay: §e%s%%',
    'beneath.nutrient.death': 'Death: §7%s%%',
    'beneath.nutrient.destruction': 'Destruction: §6%s%%',
    'beneath.nutrient.flame': 'Flame: §c%s%%',
    'beneath.nutrient.sorrow': 'Sorrow: §9%s%%',
    'beneath.block_entity.hellforge': 'Hellforge',
    'beneath.screen.juicer': 'Juicer',
    'beneath.screen.juicer.mushrooms': 'Feed me mushrooms!',
    'beneath.screen.lost_page': 'Lost Page',
    'beneath.screen.lost_page.cost': 'Cost',
    'beneath.screen.lost_page.reward': 'Reward',
    'beneath.screen.lost_page.punishment': 'Punishment',
    'beneath.lost_page.cobble': 'Any Cobblestone',
    'beneath.punishment': 'You have been given the punishment of... %s',
    'beneath.enum.punishment.none': 'No punishment at all!',
    'beneath.enum.punishment.levitation': 'Levitation!',
    'beneath.enum.punishment.drunkenness': 'A day of drunkenness!',
    'beneath.enum.punishment.blaze_inferno': 'An inferno of blazes!',
    'beneath.enum.punishment.infestation': 'An infestation of silverfish!',
    'beneath.enum.punishment.withering': 'Wither!',
    'beneath.enum.punishment.slime': 'Slime!',
    'beneath.enum.punishment.corruption': 'Corruption of the land!',
    'beneath.enum.punishment.wrath': 'Wrath from the skies!',
    'beneath.enum.punishment.champion': 'A champion rises!',
    'beneath.enum.punishment.blessing': 'A blessing! Your reward is doubled.',
    'beneath.enum.punishment.greed': 'Greed! Your page is consumed and nothing is given.',
    'beneath.enum.punishment.unknown': '§kUnknown',
    'beneath.sacrifice.error': 'Sacrifice Error: Materials not found.',
    'beneath.enchant.no_tomes': 'Enchant Error: No tomes found on nearby altars.',
    'beneath.enchant.incompatible': 'Enchant Error: No tome fits this item.',
    'beneath.enchant.no_offering': 'Enchant Error: Not enough offerings on nearby altars.',
    'beneath.enchant.cost': 'Offering: %sx %s',
    'beneath.enchant.uses': 'Uses: %s / %s',
    'beneath.enchant.punishment': 'Punishment: %s',
    'beneath.enchant.no_enchantments': 'Enchant Error: No enchantment could be rolled.',
    'config.jade.plugin_beneath.ancient_altar': 'Ancient Altar',
    'config.jade.plugin_beneath.hellforge': 'Hellforge',
    'config.jade.plugin_beneath.hellforge_side': 'Hellforge',
    'beneath.crop.too_dark': 'Too dark to grow',
    'item.beneath.juicer.filled': 'Juicer (%s)',
    'death.attack.beneath.sulfur': '%1$s mined sulfur with an iron tool and blew themselves up.',
    'death.attack.beneath.sulfur.player': '%1$s mined sulfur with an iron tool and blew themselves up while trying to escape %2$s.',
    **{'entity.beneath.boat.%s' % wood: lang('%s boat', wood) for wood in WOODS},
    **{'entity.beneath.chest_boat.%s' % wood: lang('%s chest boat', wood) for wood in WOODS},
}

VANILLA_OVERRIDE_LANG = {
    'block.minecraft.basalt': 'Basalt Column',
    'block.minecraft.polished_basalt': 'Polished Basalt Column',
}

