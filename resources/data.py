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

class Size(Enum):
    tiny = auto()
    very_small = auto()
    small = auto()
    normal = auto()
    large = auto()
    very_large = auto()
    huge = auto()


class Weight(Enum):
    very_light = auto()
    light = auto()
    medium = auto()
    heavy = auto()
    very_heavy = auto()

def generate(rm: ResourceManager):
    ### ITEM TAGS ###
    rm.item_tag('sparks_on_sulfur', *['#tfc:metal_item/%s' % metal for metal in ('black_steel', 'blue_steel', 'red_steel', 'steel', 'wrought_iron', 'cast_iron')])
    rm.item_tag('usable_in_juicer', '#tfc:foods/fruits', '#beneath:mushrooms', 'minecraft:warped_fungus', 'minecraft:crimson_fungus')
    rm.item_tag('unpostable', '#beneath:mushrooms', 'minecraft:warped_fungus', 'minecraft:crimson_fungus', 'beneath:ghost_pepper', 'beneath:gleamflower', 'minecraft:crimson_roots', 'minecraft:warped_roots', 'minecraft:nether_wart', 'minecraft:ghast_tear')

    block_and_item_tag(rm, 'tfc:rock/aqueduct', 'beneath:blackstone_aqueduct')
    rm.item_tag('tfc:rock_knapping', 'beneath:nether_pebble', 'beneath:blackstone_pebble')
    rm.item_tag('tfc:metamorphic_rock', 'beneath:blackstone_pebble')
    rm.item_tag('tfc:sedimentary_rock', 'beneath:nether_pebble')
    block_and_item_tag(rm, 'minecraft:stone_bricks', 'beneath:hellbricks')

    for shroom in MUSHROOMS:
        rm.item_tag('mushrooms', 'beneath:food/%s' % shroom)

    ### ENTITY TAGS ###
    rm.entity_tag('can_be_sacrificed', 'tfc:pig', 'tfc:goat', 'tfc:sheep')



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

def lost_page(rm: ResourceManager, name: str, cost: str, reward: str, costs: List[int], rewards: List[int], punishments: List[str], ingredient_translation: str = None):
    rm.data(('beneath', 'lost_pages', name), {
        'cost': utils.ingredient(cost),
        'reward': reward,
        'costs': costs,
        'rewards': rewards,
        'punishments': punishments,
        'ingredient_translation': ingredient_translation,
    })

def item_size(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, size: Size, weight: Weight):
    rm.data(('tfc', 'item_sizes', name_parts), {
        'ingredient': utils.ingredient(ingredient),
        'size': size.name,
        'weight': weight.name
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
