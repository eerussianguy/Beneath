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
    'death.attack.beneath.sulfur': '%1$s mined sulfur with an iron tool and blew themselves up.',
    'death.attack.beneath.sulfur.player': '%1$s mined sulfur with an iron tool and blew themselves up while trying to escape %2$s.',
    **{'entity.beneath.boat.%s' % wood: lang('%s boat', wood) for wood in WOODS},
}


