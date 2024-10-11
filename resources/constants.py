from typing import NamedTuple, List, Optional, Tuple, Dict, Set


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()

class Metal(NamedTuple):
    tier: int
    types: Set[str]
    heat_capacity_base: float  # Do not access directly, use one of specific or ingot heat capacity.
    melt_temperature: float
    melt_metal: Optional[str]

    def specific_heat_capacity(self) -> float: return round(300 / self.heat_capacity_base) / 100_000
    def ingot_heat_capacity(self) -> float: return 1 / self.heat_capacity_base

class Rock(NamedTuple):
    category: str
    sand: str

ROCK_CATEGORIES: List[str] = ['sedimentary', 'metamorphic', 'igneous_extrusive', 'igneous_intrusive']

TFC_METALS: Dict[str, Metal] = {
    'gold': Metal(1, {'part'}, 0.6, 1060, None),
}

METALS: Dict[str, Metal] = {
    'bismuth': Metal(1, {'part'}, 0.14, 270, None),
    'bismuth_bronze': Metal(2, {'part', 'tool', 'armor', 'utility'}, 0.35, 985, None),
    'black_bronze': Metal(2, {'part', 'tool', 'armor', 'utility'}, 0.35, 1070, None),
    'bronze': Metal(2, {'part', 'tool', 'armor', 'utility'}, 0.35, 950, None),
    'brass': Metal(2, {'part'}, 0.35, 930, None),
    'copper': Metal(1, {'part', 'tool', 'armor', 'utility'}, 0.35, 1080, None),
    'gold': Metal(1, {'part'}, 0.6, 1060, None),
    'nickel': Metal(1, {'part'}, 0.48, 1453, None),
    'rose_gold': Metal(1, {'part'}, 0.35, 960, None),
    'silver': Metal(1, {'part'}, 0.48, 961, None),
    'tin': Metal(1, {'part'}, 0.14, 230, None),
    'zinc': Metal(1, {'part'}, 0.21, 420, None),
    'sterling_silver': Metal(1, {'part'}, 0.35, 950, None),
    'wrought_iron': Metal(3, {'part', 'tool', 'armor', 'utility'}, 0.35, 1535, 'cast_iron'),
    'cast_iron': Metal(1, {'part'}, 0.35, 1535, None),
    'pig_iron': Metal(3, set(), 0.35, 1535, None),
    'steel': Metal(4, {'part', 'tool', 'armor', 'utility'}, 0.35, 1540, None),
    'black_steel': Metal(5, {'part', 'tool', 'armor', 'utility'}, 0.35, 1485, None),
    'blue_steel': Metal(6, {'part', 'tool', 'armor', 'utility'}, 0.35, 1540, None),
    'red_steel': Metal(6, {'part', 'tool', 'armor', 'utility'}, 0.35, 1540, None),
    'weak_steel': Metal(4, set(), 0.35, 1540, None),
    'weak_blue_steel': Metal(5, set(), 0.35, 1540, None),
    'weak_red_steel': Metal(5, set(), 0.35, 1540, None),
    'high_carbon_steel': Metal(3, set(), 0.35, 1540, 'pig_iron'),
    'high_carbon_black_steel': Metal(4, set(), 0.35, 1540, 'weak_steel'),
    'high_carbon_blue_steel': Metal(5, set(), 0.35, 1540, 'weak_blue_steel'),
    'high_carbon_red_steel': Metal(5, set(), 0.35, 1540, 'weak_red_steel'),
    'unknown': Metal(0, set(), 0.5, 400, None)
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
                'crackrack_rock', 'juicer', 'raw_slime', 'lost_page'
                )
MUSHROOMS = ('button', 'chantrelle', 'death_cap', 'destroying_angels', 'fools_funnel', 'oyster', 'parasol', 'portobello', 'shittake', 'sulfur_tuft')
POISONOUS_MUSHROOMS = ('death_cap', 'destroying_angels', 'fools_funnel', 'sulfur_tuft')

ROCK_SPIKE_PARTS = ('base', 'middle', 'tip')

SPAWN_EGG_ENTITIES = ('red_elk',)

DISABLED_VANILLA_RECIPES = ('polished_blackstone_bricks', 'polished_blackstone_button', 'polished_blackstone_pressure_plate', 'cracked_polished_blackstone_bricks', 'nether_bricks')

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
    'beneath.lost_page.stone_bricks': 'Any Stone Bricks',
    'beneath.punishment': 'You have been given the punishment of... %s',
    'beneath.enum.punishment.none': 'No punishment at all!',
    'beneath.enum.punishment.levitation': 'Levitation!',
    'beneath.enum.punishment.drunkenness': 'A day of drunkenness!',
    'beneath.enum.punishment.blaze_inferno': 'An inferno of blazes!',
    'beneath.enum.punishment.infestation': 'An infestation of silverfish!',
    'beneath.enum.punishment.withering': 'Wither!',
    'beneath.enum.punishment.slime': 'Slime!',
    'beneath.sacrifice.error': 'Sacrifice Error: Materials not found.',
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

