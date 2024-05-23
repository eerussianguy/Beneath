from typing import Union, Any, Literal, get_args

from mcresources import ResourceManager, utils
from mcresources.type_definitions import ResourceIdentifier, JsonObject, Json, VerticalAnchor

from constants import *

def generate(rm: ResourceManager):
    configured_placed_feature(rm, 'nether_spikes', 'beneath:nether_spikes', {'raw': 'minecraft:netherrack', 'spike': 'beneath:haunted_spike'}, decorate_count(128), decorate_square(), decorate_range_10_10(), decorate_biome())
    configured_placed_feature(rm, 'glowstone_spikes', 'beneath:large_nether_spikes', {'raw': 'minecraft:glowstone', 'spike': 'beneath:glowstone_spike'}, decorate_chance(6), decorate_count(16), decorate_square(), decorate_range_10_10(), decorate_biome())
    configured_placed_feature(rm, 'nether_pebble', 'minecraft:simple_block', {'to_place': random_property_provider('beneath:nether_pebble', 'count')}, decorate_replaceable(), decorate_would_survive('beneath:nether_pebble'), decorate_air())
    configured_placed_feature(rm, 'nether_pebble_patch', 'minecraft:random_patch', random_config('beneath:nether_pebble', 16, 10, 1), decorate_chance(3), decorate_every_layer(5), decorate_biome())
    configured_placed_feature(rm, 'blackstone_pebble', 'minecraft:simple_block', {'to_place': random_property_provider('beneath:blackstone_pebble', 'count')}, decorate_replaceable(), decorate_would_survive('beneath:nether_pebble'), decorate_air())
    configured_placed_feature(rm, 'blackstone_pebble_patch', 'minecraft:random_patch', random_config('beneath:blackstone_pebble', 16, 10, 1), decorate_chance(3), decorate_every_layer(2), decorate_biome())
    configured_placed_feature(rm, 'sulfur', 'minecraft:simple_block', {'to_place': simple_state_provider('beneath:sulfur')}, decorate_replaceable(), decorate_would_survive('beneath:sulfur'), decorate_air())
    configured_placed_feature(rm, 'sulfur_patch', 'minecraft:random_patch', random_config('beneath:sulfur', 8, 5, 1), decorate_chance(14), decorate_every_layer(5), decorate_near_lava(5), decorate_biome())
    configured_placed_feature(rm, 'blackstone_boulders', 'beneath:nether_boulders', {'blocks': weighted_list([('minecraft:blackstone', 50), ('minecraft:basalt', 10), ('minecraft:gilded_blackstone', 1)])}, decorate_chance(24), decorate_every_layer(1), decorate_flat_enough(0.4, 2, 4), decorate_biome())
    configured_placed_feature(rm, 'cobble_boulders', 'beneath:nether_boulders', {'blocks': weighted_list([('minecraft:netherrack', 10), ('beneath:cobblerack', 20), ('beneath:fungal_cobblerack', 10)])}, decorate_chance(24), decorate_every_layer(1), decorate_flat_enough(0.4, 3, 4), decorate_biome())
    configured_placed_feature(rm, 'amethyst_geode', 'tfc:geode', {'outer': 'minecraft:blackstone', 'middle': 'tfc:rock/raw/quartzite', 'inner': [{'data': 'tfc:ore/amethyst/quartzite', 'weight': 1}, {'data': 'tfc:rock/raw/quartzite', 'weight': 2}]}, decorate_chance(100), decorate_above_lava_level(), decorate_square())
    configured_placed_feature(rm, 'soul_clay_disc', 'tfc:soil_disc', {'states': [{'replace': 'minecraft:soul_sand', 'with': 'beneath:soul_clay'}], 'min_radius': 3, 'max_radius': 5, 'height': 3}, decorate_chance(20), decorate_every_layer(1), decorate_flat_enough(0.4, 2, 4))
    configured_placed_feature(rm, 'delta', 'minecraft:delta_feature', {'contents': utils.block_state('minecraft:lava[level=0]'), 'rim': utils.block_state('tfc:rock/magma/basalt'), 'rim_size': uniform_int(3, 7), 'size': uniform_int(0, 2)}, decorate_every_layer(40), decorate_biome())
    configured_placed_feature(rm, 'gleamflower', 'minecraft:simple_block', {'to_place': simple_state_provider('beneath:gleamflower')}, decorate_replaceable(), decorate_would_survive('beneath:gleamflower'), decorate_air())
    configured_placed_feature(rm, 'gleamflower_patch', 'minecraft:random_patch', random_config('beneath:gleamflower', 5, 5, 1), decorate_chance(4), decorate_every_layer(1), decorate_biome())
    configured_placed_feature(rm, 'burpflower', 'minecraft:simple_block', {'to_place': simple_state_provider('beneath:burpflower')}, decorate_replaceable(), decorate_would_survive('beneath:burpflower'), decorate_air())
    configured_placed_feature(rm, 'burpflower_patch', 'minecraft:random_patch', random_config('beneath:burpflower', 5, 5, 1), decorate_chance(20), decorate_every_layer(1), decorate_biome())

    for rock, rock_data in ROCKS.items():
        if rock_data.category == 'igneous_extrusive':
            configured_placed_feature(rm, 'magma_' + rock, 'minecraft:ore', {'discard_chance_on_air_exposure': 0, 'size': 33, 'targets': [{'state': utils.block_state('tfc:rock/magma/%s' % rock), 'target': {'block': 'minecraft:netherrack', 'predicate_type': 'minecraft:block_match'}}]}, decorate_chance(10), decorate_count(4), decorate_square(), decorate_range(36, 27), decorate_biome())

    configured_placed_feature(rm, 'tree/crimson', 'tfc:random_tree', {
        'structures': ['beneath:crimson/%s' % i for i in range(1, 17)],
        'radius': 1,
        'placement': {'width': 1, 'height': 7},
        'trunk': {
            'state': utils.block_state('beneath:wood/log/crimson[axis=y,branch_direction=none]'),
            'min_height': 3,
            'max_height': 5,
            'wide': False
        }
    }, decorate_every_layer(8), decorate_biome())
    configured_placed_feature(rm, 'tree/warped', 'tfc:random_tree', {
        'structures': ['beneath:warped/%s' % i for i in range(1, 18)],
        'radius': 1,
        'placement': {'width': 1, 'height': 5},
        'trunk': {
            'state': utils.block_state('beneath:wood/log/warped[axis=y,branch_direction=none]'),
            'min_height': 3,
            'max_height': 5,
            'wide': False
        }
    }, decorate_every_layer(8), decorate_biome())

    configured_placed_feature(rm, 'vein/quartz', 'tfc:cluster_vein', {
        'rarity': 30,
        'size': 15,
        'density': 0.6,
        'min_y': 1,
        'max_y': 127,
        'random_name': 'quartz',
        'blocks': [{'replace': ['minecraft:netherrack'], 'with': [{'weight': 1, 'block': 'minecraft:nether_quartz_ore'}]}]
    })

    configured_placed_feature(rm, 'vein/sylvite', 'tfc:cluster_vein', {
        'rarity': 35,
        'size': 17,
        'density': 0.6,
        'min_y': 1,
        'max_y': 127,
        'random_name': 'sylvite',
        'blocks': [{'replace': ['minecraft:netherrack'], 'with': [{'weight': 1, 'block': 'beneath:ore/blackstone_sylvite'}]}]
    })

    configured_placed_feature(rm, 'vein/normal_gold', 'tfc:cluster_vein', {
        'rarity': 30,
        'size': 15,
        'density': 0.5,
        'min_y': 64,
        'max_y': 127,
        'random_name': 'normal_gold',
        'blocks': [{'replace': ['minecraft:netherrack'], 'with': [
            {'weight': 30, 'block': 'beneath:ore/poor_nether_gold'},
            {'weight': 15, 'block': 'beneath:ore/normal_nether_gold'},
            {'weight': 5, 'block': 'beneath:ore/rich_nether_gold'},
            {'weight': 7, 'block': 'beneath:ore/nether_pyrite'},
        ]}]
    })

    configured_placed_feature(rm, 'vein/deep_gold', 'tfc:cluster_vein', {
        'rarity': 81,
        'size': 17,
        'density': 0.6,
        'min_y': 1,
        'max_y': 32,
        'random_name': 'deep_gold',
        'blocks': [{'replace': ['minecraft:netherrack'], 'with': [
            {'weight': 5, 'block': 'beneath:ore/poor_nether_gold'},
            {'weight': 10, 'block': 'beneath:ore/normal_nether_gold'},
            {'weight': 3, 'block': 'beneath:ore/rich_nether_gold'},
        ]}]
    })

    configured_placed_feature(rm, 'vein/cursecoal', 'tfc:disc_vein', {
        'rarity': 45,
        'size': 13,
        'height': 4,
        'density': 0.88,
        'min_y': 80,
        'max_y': 127,
        'project': False,
        'random_name': 'cursecoal',
        'blocks': [{'replace': ['minecraft:netherrack'], 'with': [{'weight': 1, 'block': 'beneath:ore/nether_cursecoal'}]}]
    })

    configured_placed_feature(rm, 'vein/crackrack_pipe', 'tfc:pipe_vein', {
        'rarity': 45,
        'size': 50,
        'density': 0.98,
        'min_y': 1,
        'max_y': 127,
        'random_name': 'crackrack_pipe',
        'height': 120,
        'radius': 9,
        'min_skew': 7,
        'max_skew': 20,
        'min_slant': 2,
        'max_slant': 5,
        'project': False,
        'sign': 0,
        'blocks': [{'replace': ['minecraft:netherrack'], 'with': [{'weight': 1, 'block': 'beneath:crackrack'}]}]
    })

    rm.placed_feature_tag('veins', *['beneath:vein/%s' % v for v in ('quartz', 'sylvite', 'normal_gold', 'deep_gold', 'cursecoal', 'crackrack_pipe')])
    rm.placed_feature_tag('underground_decoration', '#beneath:veins', *['beneath:magma_%s' % r for r, d in ROCKS.items() if d.category == 'igneous_extrusive'], 'beneath:amethyst_geode')
    rm.placed_feature_tag('everywhere_but_basalt_deltas', 'beneath:nether_pebble_patch', 'beneath:blackstone_boulders', 'beneath:cobble_boulders', 'beneath:sulfur_patch')

    rm.placed_feature_tag('vegetal_decoration/nether_wastes', 'beneath:nether_spikes', 'beneath:glowstone_spikes')
    rm.placed_feature_tag('surface_structures/nether_wastes', 'beneath:nether_spikes', 'beneath:glowstone_spikes')

    rm.placed_feature_tag('vegetal_decoration/crimson_forest', 'beneath:tree/crimson', '#beneath:everywhere_but_basalt_deltas', 'beneath:gleamflower_patch', 'beneath:burpflower_patch')
    rm.placed_feature_tag('vegetal_decoration/warped_forest', 'beneath:tree/warped', '#beneath:everywhere_but_basalt_deltas', 'beneath:gleamflower_patch', 'beneath:burpflower_patch')

    rm.placed_feature_tag('surface_structures/basalt_deltas', 'beneath:delta')
    rm.placed_feature_tag('vegetal_decoration/basalt_deltas', 'beneath:blackstone_pebble_patch')

    rm.placed_feature_tag('vegetal_decoration/soul_sand_valley', 'beneath:blackstone_pebble_patch', '#beneath:everywhere_but_basalt_deltas')

    rm.placed_feature_tag('removed_features', *['minecraft:%s' % f for f in ('crimson_fungi', 'warped_fungi', 'brown_mushroom_normal', 'red_mushroom_normal', 'ore_quartz_nether', 'ore_quartz_deltas', 'ore_gold_nether', 'ore_gold_deltas', 'ore_ancient_debris_large', 'ore_debris_small', 'ore_magma', 'red_mushroom_nether', 'brown_mushroom_nether', 'delta')])

    add_feature(rm, 'underground_decoration', '#minecraft:is_nether', '#beneath:underground_decoration', 'underground_decoration')
    add_feature(rm, 'vegetal_nether_wastes', 'minecraft:nether_wastes', '#beneath:vegetal_decoration/nether_wastes', 'vegetal_decoration')
    add_feature(rm, 'surface_nether_wastes', 'minecraft:nether_wastes', '#beneath:surface_structures/nether_wastes', 'surface_structures')
    add_feature(rm, 'vegetal_crimson_forest', 'minecraft:crimson_forest', '#beneath:vegetal_decoration/crimson_forest', 'vegetal_decoration')
    add_feature(rm, 'vegetal_warped_forest', 'minecraft:warped_forest', '#beneath:vegetal_decoration/warped_forest', 'vegetal_decoration')
    add_feature(rm, 'surface_basalt_deltas', 'minecraft:basalt_deltas', '#beneath:surface_structures/basalt_deltas', 'surface_structures')
    add_feature(rm, 'vegetal_basalt_deltas', 'minecraft:basalt_deltas', '#beneath:vegetal_decoration/basalt_deltas', 'vegetal_decoration')
    add_feature(rm, 'vegetal_soul_sand_valley', 'minecraft:soul_sand_valley', '#beneath:vegetal_decoration/soul_sand_valley', 'vegetal_decoration')

    remove_feature(rm, 'nether_removals', '#minecraft:is_nether', '#beneath:removed_features')

def add_feature(rm: ResourceManager, filename: str, biome: str, features: str, step: str):
    rm.data(('forge', 'biome_modifier', filename), {
        'type': 'forge:add_features',
        'biomes': biome,
        'features': features,
        'step': step
    })

def remove_feature(rm: ResourceManager, filename: str, biome: str, features: str, step: str = None):
    rm.data(('forge', 'biome_modifier', filename), {
        'type': 'forge:remove_features',
        'biomes': biome,
        'features': features,
        'steps': step
    })

def configured_placed_feature(rm: ResourceManager, name_parts: ResourceIdentifier, feature: Optional[ResourceIdentifier] = None, config: JsonObject = None, *placements: Json):
    res = utils.resource_location(rm.domain, name_parts)
    if feature is None:
        feature = res
    rm.configured_feature(res, feature, config)
    rm.placed_feature(res, res, *placements)

Heightmap = Literal['motion_blocking', 'motion_blocking_no_leaves', 'ocean_floor', 'ocean_floor_wg', 'world_surface', 'world_surface_wg']
HeightProviderType = Literal['constant', 'uniform', 'biased_to_bottom', 'very_biased_to_bottom', 'trapezoid', 'weighted_list']

def decorate_range(min_y: VerticalAnchor, max_y: VerticalAnchor, bias: HeightProviderType = 'uniform') -> Json:
    return {
        'type': 'minecraft:height_range',
        'height': height_provider(min_y, max_y, bias)
    }

def height_provider(min_y: VerticalAnchor, max_y: VerticalAnchor, height_type: HeightProviderType = 'uniform') -> Dict[str, Any]:
    assert height_type in get_args(HeightProviderType)
    return {
        'type': height_type,
        'min_inclusive': utils.as_vertical_anchor(min_y),
        'max_inclusive': utils.as_vertical_anchor(max_y)
    }

def uniform_int(min_inclusive: int, max_inclusive: int) -> Dict[str, Any]:
    return {
        'type': 'uniform',
        'value': {
            'min_inclusive': min_inclusive,
            'max_inclusive': max_inclusive
        }
    }

def decorate_flat_enough(flatness: float = None, radius: int = None, max_depth: int = None):
    return {'type': 'tfc:flat_enough', 'flatness': flatness, 'radius': radius, 'max_depth': max_depth}

def weighted_list(tuples: List[Tuple[str, int]]) -> Json:
    return [weighted(b, w) for b, w in tuples]

def weighted(block: str, weight: int) -> Json:
    return {'block': block, 'weight': weight}

def random_config(feature: str, tries: int = None, xz_spread: int = None, y_spread: int = None) -> Json:
    return {
        'tries': tries,
        'xz_spread': xz_spread,
        'y_spread': y_spread,
        'feature': feature
    }

def decorate_near_lava(radius: int) -> Json:
    return {
        'type': 'tfc:near_fluid',
        'fluids': ['minecraft:lava'],
        'radius': radius
    }

def decorate_would_survive(block: str) -> Json:
    return decorate_block_predicate({
        'type': 'would_survive',
        'state': utils.block_state(block)
    })

def decorate_air():
    return decorate_block_predicate({'type': 'minecraft:matching_blocks', 'blocks': 'minecraft:air'})

def simple_state_provider(name: str) -> Dict[str, Any]:
    return {'type': 'minecraft:simple_state_provider', 'state': utils.block_state(name)}

def decorate_replaceable() -> Json:
    return decorate_block_predicate({'type': 'tfc:replaceable'})

def decorate_block_predicate(predicate: Json) -> Json:
    return {
        'type': 'block_predicate_filter',
        'predicate': predicate
    }

def decorate_count(count: int) -> Json:
    return {'type': 'minecraft:count', 'count': count}

def decorate_square() -> Json:
    return 'minecraft:in_square'

def decorate_biome() -> Json:
    return 'minecraft:biome'

def decorate_range_10_10() -> Json:
    return {
        'type': 'minecraft:height_range',
        'height': {
            'type': 'minecraft:uniform',
            'max_inclusive': {
                'below_top': 10
            },
            'min_inclusive': {
                'above_bottom': 10
            }
        }
    }

def decorate_above_lava_level() -> Json:
    return {
        'type': 'minecraft:height_range',
        'height': {
            'type': 'minecraft:uniform',
            'max_inclusive': {
                'below_top': 10
            },
            'min_inclusive': {
                'above_bottom': 32
            }
        }
    }


def random_property_provider(name: str, prop: str) -> Json:
    return {
        'type': 'tfc:random_property',
        'state': utils.block_state(name), 'property': prop
    }

def decorate_every_layer(count: int) -> Json:
    return {
        'type': 'minecraft:count_on_every_layer',
        'count': count
    }

def decorate_chance(rarity_or_probability: Union[int, float]) -> Json:
    return {'type': 'minecraft:rarity_filter', 'chance': round(1 / rarity_or_probability) if isinstance(rarity_or_probability, float) else rarity_or_probability}
