from typing import Optional, Union

from mcresources import ResourceManager, utils, RecipeContext
from mcresources.type_definitions import ResourceIdentifier, Json

from constants import *

def generate(rm: ResourceManager):
    ### CRAFTING RECIPES ###
    rm.crafting_shaped('crafting/cobblerack', ['XX', 'XX'], {'X': 'beneath:nether_pebble'}, 'beneath:cobblerack').with_advancement('beneath:nether_pebble')
    rm.crafting_shaped('crafting/crimson_thatch', ['XX', 'XX'], {'X': 'beneath:warped_straw'}, 'beneath:crimson_thatch').with_advancement('beneath:crimson_strawr')
    rm.crafting_shaped('crafting/warped_thatch', ['XX', 'XX'], {'X': 'beneath:warped_straw'}, 'beneath:warped_thatch').with_advancement('beneath:warped_straw')
    rm.crafting_shaped('crafting/blackstone', ['XX', 'XX'], {'X': 'beneath:blackstone_pebble'}, 'minecraft:blackstone').with_advancement('beneath:blackstone_pebble')
    rm.crafting_shaped('crafting/blackstone_bricks', ['XMX', 'MXM', 'XMX'], {'X': 'beneath:blackstone_brick', 'M': '#tfc:mortar'}, (4, 'minecraft:polished_blackstone_bricks')).with_advancement('beneath:blackstone_brick')
    damage_shapeless(rm, 'crafting/blackstone_brick', ('beneath:blackstone_pebble', '#tfc:chisels'), 'beneath:blackstone_brick').with_advancement('beneath:blackstone_pebble')
    damage_shapeless(rm, 'crafting/blackstone_button', ('beneath:blackstone_brick', '#tfc:chisels'), 'minecraft:polished_blackstone_button').with_advancement('beneath:blackstone_brick')
    damage_shapeless(rm, 'crafting/blackstone_plate', ('beneath:blackstone_brick', 'beneath:blackstone_brick', '#tfc:chisels'), 'minecraft:polished_blackstone_pressure_plate').with_advancement('beneath:blackstone_brick')
    rm.crafting_shaped('crafting/blackstone_aqueduct', ['X X', 'MXM'], {'X': 'beneath:blackstone_brick', 'M': '#tfc:mortar'}, 'beneath:blackstone_aqueduct').with_advancement('beneath:blackstone_brick')
    damage_shapeless(rm, 'crafting/cracked_blackstone_bricks', ('minecraft:polished_blackstone_bricks', '#tfc:hammers'), 'minecraft:cracked_polished_blackstone_bricks').with_advancement('minecraft:polished_blackstone_bricks')
    rm.crafting_shapeless('crafting/agonizing_fertilizer', tuple('beneath:pure_%s' % n for n in NUTRIENTS), 'beneath:agonizing_fertilizer').with_advancement('beneath:pure_flame')
    rm.crafting_shapeless('crafting/cursed_hide_change', ('beneath:cursed_hide', 'tfc:powder/flux', 'tfc:powder/flux', 'minecraft:white_dye'), 'tfc:large_raw_hide').with_advancement('beneath:cursed_hide')

    ### HEATING RECIPES ###
    metal_data = TFC_METALS['gold']
    heat_recipe(rm, 'gold_chunk', 'beneath:gold_chunk', metal_data.melt_temperature, None, '100 tfc:metal/gold')

    ### COLLAPSE RECIPES ###
    # Note: TRIGGERS are auto-added by TFC to CAN_COLLAPSE

    # Nether bricks -- any can trigger, only solid blocks will start
    rm.block_tag('nether_brick_decor', *['minecraft:%snether_brick_%s' % (pref, variant) for pref in ('', 'red_') for variant in ('slab', 'stairs', 'wall', 'fence') if not (variant == 'fence' and pref == 'red_')])
    rm.block_tag('nether_bricks', 'minecraft:nether_bricks', 'minecraft:red_nether_bricks')
    rm.block_tag('tfc:can_trigger_collapse', '#beneath:nether_bricks', '#beneath:nether_brick_decor')
    rm.block_tag('tfc:can_start_collapse', '#beneath:nether_bricks')
    collapse_recipe(rm, 'nether_bricks', {'tag': 'beneath:nether_bricks'}, copy_input=True)
    collapse_recipe(rm, 'nether_brick_decor', {'tag': 'beneath:nether_brick_decor'}, copy_input=True)
    # Blackstone bricks -- same thing
    rm.block_tag('blackstone_decor', *['minecraft:%s%s' % (pref, variant) for pref in ('blackstone_', 'polished_blackstone_', 'polished_blackstone_brick_') for variant in ('slab', 'stairs', 'wall')])
    rm.block_tag('blackstone', *['minecraft:%s' % b for b in ('blackstone', 'polished_blackstone', 'polished_blackstone_bricks', 'chiseled_polished_blackstone', 'cracked_polished_blackstone_bricks', 'gilded_blackstone')])
    rm.block_tag('tfc:can_trigger_collapse', '#beneath:blackstone', '#beneath:blackstone_decor')
    rm.block_tag('tfc:can_start_collapse', '#beneath:blackstone')
    collapse_recipe(rm, 'blackstone', {'tag': 'beneath:blackstone'}, copy_input=True)
    collapse_recipe(rm, 'blackstone_decor', {'tag': 'beneath:blackstone_decor'}, copy_input=True)
    # Spikes
    rm.block_tag('tfc:can_collapse', 'beneath:haunted_spike')
    collapse_recipe(rm, 'haunted_spike', 'beneath:haunted_spike', copy_input=True)
    rm.block_tag('tfc:can_collapse', 'beneath:glowstone_spike')
    collapse_recipe(rm, 'glowstone_spike', 'beneath:glowstone_spike', copy_input=True)

    # Misc
    basic_collapsible(rm, 'cobblerack', 'beneath:cobblerack', copy_input=True)
    basic_landslide(rm, 'cobblerack', 'beneath:cobblerack')
    basic_collapsible(rm, 'fungal_cobblerack', 'beneath:fungal_cobblerack', copy_input=True)
    basic_landslide(rm, 'fungal_cobblerack', 'beneath:fungal_cobblerack')
    basic_collapsible(rm, 'glowstone', 'minecraft:glowstone', copy_input=True)  # todo: make it shatter on landing always
    basic_collapsible(rm, 'basalt', 'minecraft:basalt', copy_input=True)

    for recipe in DISABLED_VANILLA_RECIPES:
        disable_recipe(rm, 'minecraft:' + recipe)


def heat_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, temperature: float, result_item: Optional[Union[str, Json]] = None, result_fluid: Optional[str] = None, use_durability: Optional[bool] = None) -> RecipeContext:
    result_item = item_stack_provider(result_item) if isinstance(result_item, str) else result_item
    result_fluid = None if result_fluid is None else fluid_stack(result_fluid)
    return rm.recipe(('heating', name_parts), 'tfc:heating', {
        'ingredient': utils.ingredient(ingredient),
        'result_item': result_item,
        'result_fluid': result_fluid,
        'temperature': temperature,
        'use_durability': use_durability if use_durability else None,
    })

def item_stack_provider(
    data_in: Json = None,
    # Possible Modifiers
    copy_input: bool = False,
    copy_heat: bool = False,
    copy_food: bool = False,  # copies both decay and traits
    copy_oldest_food: bool = False,  # copies only decay, from all inputs (uses crafting container)
    reset_food: bool = False,  # rest_food modifier - used for newly created food from non-food
    add_heat: float = None,
    add_trait: str = None,  # applies a food trait and adjusts decay accordingly
    remove_trait: str = None,  # removes a food trait and adjusts decay accordingly
    empty_bowl: bool = False,  # replaces a soup with its bowl
    copy_forging: bool = False,
    add_bait_to_rod: bool = False,  # adds bait to the rod, uses crafting container
    sandwich: bool = False,  # builds a sandwich form inputs, uses crafting container
    dye_color: str = None  # applies a dye color to leather dye-able armor
) -> Json:
    if isinstance(data_in, dict):
        return data_in
    stack = utils.item_stack(data_in) if data_in is not None else None
    modifiers = [k for k, v in (
        # Ordering is important here
        # First, modifiers that replace the entire stack (copy input style)
        # Then, modifiers that only mutate an existing stack
        ('tfc:empty_bowl', empty_bowl),
        ('tfc:sandwich', sandwich),
        ('tfc:copy_input', copy_input),
        ('tfc:copy_heat', copy_heat),
        ('tfc:copy_food', copy_food),
        ('tfc:copy_oldest_food', copy_oldest_food),
        ('tfc:reset_food', reset_food),
        ('tfc:copy_forging_bonus', copy_forging),
        ('tfc:add_bait_to_rod', add_bait_to_rod),
        ({'type': 'tfc:add_heat', 'temperature': add_heat}, add_heat is not None),
        ({'type': 'tfc:add_trait', 'trait': add_trait}, add_trait is not None),
        ({'type': 'tfc:remove_trait', 'trait': remove_trait}, remove_trait is not None),
        ({'type': 'tfc:dye_leather', 'color': dye_color}, dye_color is not None)
    ) if v]
    if modifiers:
        return {
            'stack': stack,
            'modifiers': modifiers
        }
    return stack

def fluid_stack(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return data_in
    fluid, tag, amount, _ = utils.parse_item_stack(data_in, False)
    assert not tag, 'fluid_stack() cannot be a tag'
    return {
        'fluid': fluid,
        'amount': amount
    }


def damage_shapeless(rm: ResourceManager, name_parts: ResourceIdentifier, ingredients: Json, result: Json, group: str = None, conditions: utils.Json = None) -> RecipeContext:
    res = utils.resource_location(rm.domain, name_parts)
    rm.write((*rm.resource_dir, 'data', res.domain, 'recipes', res.path), {
        'type': 'tfc:damage_inputs_shapeless_crafting',
        'recipe': {
            'type': 'minecraft:crafting_shapeless',
            'group': group,
            'ingredients': utils.item_stack_list(ingredients),
            'result': utils.item_stack(result),
            'conditions': utils.recipe_condition(conditions)
        }
    })
    return RecipeContext(rm, res)

def basic_landslide(rm: ResourceManager, name: str, block: str):
    rm.block_tag('tfc:can_landslide', block)
    landslide_recipe(rm, name, block, block)

def basic_collapsible(rm: ResourceManager, name: str, block: str, result: Optional[utils.Json] = None, copy_input: Optional[bool] = None):
    collapse_recipe(rm, name, block, result, copy_input)
    rm.block(block).with_tag('tfc:can_trigger_collapse').with_tag('tfc:can_start_collapse')

def landslide_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, result: utils.Json):
    rm.recipe(('landslide', name_parts), 'tfc:landslide', {
        'ingredient': ingredient,
        'result': result
    })

def disable_recipe(rm: ResourceManager, name_parts: ResourceIdentifier):
    # noinspection PyTypeChecker
    rm.recipe(name_parts, None, {}, conditions='forge:false')

def collapse_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient, result: Optional[utils.Json] = None, copy_input: Optional[bool] = None):
    assert result is not None or copy_input
    rm.recipe(('collapse', name_parts), 'tfc:collapse', {
        'ingredient': ingredient,
        'result': result,
        'copy_input': copy_input
    })
