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

WOODS = ['crimson', 'warped']

TREE_SAPLING_DROP_CHANCES = {
    'crimson': 0.0428,
    'warped': 0.0115,
}

NUTRIENTS = ('death', 'destruction', 'decay', 'sorrow', 'flame')

SIMPLE_ITEMS = ('gold_chunk', 'cursed_hide', 'cursecoal', 'pure_death', 'pure_decay', 'pure_destruction', 'pure_sorrow', 'pure_flame', 'agonizing_fertilizer', 'crimson_straw', 'warped_straw', 'ghost_pepper', 'blackstone_brick',
                'crackrack_rock',
                )

ROCK_SPIKE_PARTS = ('base', 'middle', 'tip')

SPAWN_EGG_ENTITIES = ('red_elk',)

DISABLED_VANILLA_RECIPES = ('polished_blackstone_bricks', 'polished_blackstone_button', 'polished_blackstone_pressure_plate', 'cracked_polished_blackstone_bricks')

DEFAULT_CROPS = {
    'crimson_roots': 4,
    'ghost_pepper': 7,
    'gleamflower': 4,
    'nether_wart': 4,
    'warped_roots': 4
}

DEFAULT_LANG = {
    'itemGroup.beneath.beneath': 'Beneath Items',
    'entity.beneath.leviathan_fireball': 'Leviathan Fireball',
    'entity.beneath.blaze_leviathan': 'Blaze Leviathan',
    'entity.beneath.red_elk': 'Red Elk',
    'beneath.nutrient.decay': 'Decay: §e%s%%',
    'beneath.nutrient.death': 'Death: §7%s%%',
    'beneath.nutrient.destruction': 'Destruction: §6%s%%',
    'beneath.nutrient.flame': 'Flame: §c%s%%',
    'beneath.nutrient.sorrow': 'Sorrow: §9%s%%',
    'beneath.block_entity.hellforge': 'Hellforge',
    'death.attack.beneath.sulfur': '%1$s mined sulfur with an iron tool and blew themselves up.',
    'death.attack.beneath.sulfur.player': '%1$s mined sulfur with an iron tool and blew themselves up while trying to escape %2$s.',
    **{'entity.beneath.boat.%s' % wood: lang('%s boat', wood) for wood in WOODS},
}


