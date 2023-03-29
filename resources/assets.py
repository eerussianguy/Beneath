from typing import Any

from mcresources import ResourceManager, utils, loot_tables, ItemContext, BlockContext

from constants import *

def generate(rm: ResourceManager):
    rm.lang(DEFAULT_LANG)

    for item in SIMPLE_ITEMS:
        rm.item_model(item).with_lang(lang(item))
    for creature in SPAWN_EGG_ENTITIES:
        rm.item_model('spawn_egg/%s' % creature, parent='minecraft:item/template_spawn_egg', no_textures=True).with_lang(lang('%s Spawn Egg', creature))

    for ore in ('nether_gold',):
        for grade in ('poor', 'normal', 'rich'):
            block = rm.blockstate('ore/%s_%s' % (grade, ore), model='beneath:block/ore/%s_%s' % (grade, ore))
            block.with_block_model(textures={'all': 'beneath:block/%s_%s' % (grade, ore)}).with_item_model()
            block.with_lang(lang('%s %s ore', grade, ore))
            block.with_tag('tfc:prospectable').with_tag('minecraft:mineable/pickaxe').with_tag('tfc:needs_copper_tool')
            rm.block('beneath:ore/%s_%s/prospected' % (grade, ore)).with_lang(lang(ore))

            if ore == 'nether_gold':
                block.with_block_loot('tfc:ore/%s_native_gold' % grade)

    for ore in ('nether_pyrite', 'blackstone_sylvite', 'nether_cursecoal'):
        block = rm.blockstate('ore/%s' % ore, model='beneath:block/ore/%s' % ore)
        block.with_block_model(textures={'all': 'beneath:block/%s' % ore}).with_item_model()
        block.with_lang(lang('%s ore', ore))
        block.with_tag('tfc:prospectable').with_tag('minecraft:mineable/pickaxe').with_tag('tfc:needs_copper_tool')
        rm.block('beneath:ore/%s/prospected' % ore).with_lang(lang(ore))

        if ore == 'nether_pyrite':
            block.with_block_loot('tfc:ore/pyrite')
        elif ore == 'blackstone_sylvite':
            block.with_block_loot('tfc:ore/sylvite')
        elif ore == 'nether_cursecoal':
            block.with_block_loot('beneath:cursecoal')

    rm.block_tag('tfc:prospectable', 'minecraft:nether_quartz_ore')
    rm.block('minecraft:nether_quartz_ore/prospected').with_lang(lang('nether quartz'))

    for rock in ('haunted', 'glowstone',):
        block = rm.blockstate('%s_spike' % rock, variants=dict(('part=%s' % part, {'model': 'beneath:block/spike/%s_%s' % (rock, part)}) for part in ROCK_SPIKE_PARTS))
        block.with_lang(lang('%s spike', rock)).with_tag('minecraft:mineable/pickaxe')
        if rock == 'haunted':
            block.with_block_loot('1-2 beneath:nether_pebble')
        elif rock == 'glowstone':
            block.with_block_loot('1-2 minecraft:glowstone_dust')

        # Individual models
        rm.item_model('%s_spike' % rock, 'beneath:block/%s_spike' % rock, parent='beneath:block/spike/%s_base' % rock)
        for part in ROCK_SPIKE_PARTS:
            rm.block_model(('spike', '%s_%s' % (rock, part)), {
                'texture': 'beneath:block/%s_spike' % rock,
                'particle': 'beneath:block/%s_spike' % rock
            }, parent='tfc:block/rock/spike_%s' % part)

    for pebble in ('nether_pebble', 'blackstone_pebble',):
        block = rm.blockstate(pebble, variants={
            'count=1': four_ways('beneath:block/%s_pebble' % pebble),
            'count=2': four_ways('beneath:block/%s_rubble' % pebble),
            'count=3': four_ways('beneath:block/%s_boulder' % pebble)
        }, use_default_model=False)

        for loose_type in ('pebble', 'rubble', 'boulder'):
            rm.block_model('%s_%s' % (pebble, loose_type), 'beneath:item/%s' % pebble, parent='tfc:block/groundcover/%s' % loose_type)

        block.with_lang(lang(pebble)).with_tag('can_be_snow_piled').with_block_loot({
            'name': 'beneath:' + pebble,
            'functions': [
                {**loot_tables.set_count(2), 'conditions': [loot_tables.block_state_property('beneath:%s[count=2]' % pebble)]},
                {**loot_tables.set_count(3), 'conditions': [loot_tables.block_state_property('beneath:%s[count=3]' % pebble)]},
                loot_tables.explosion_decay()
            ]
        })
        rm.item_model(pebble, 'beneath:item/%s' % pebble)

    for i in range(0, 4):
        sulf = 'sulfur%s' % i
        rm.block_model(sulf, {'0': 'beneath:block/%s' % sulf, 'particle': 'beneath:block/%s' % sulf}, parent='tfc:block/groundcover/guano')
    rm.blockstate('sulfur', variants={'': [{'model': 'beneath:block/sulfur%s' % i} for i in range(0, 4)]}, use_default_model=False).with_block_loot('tfc:powder/sulfur').with_lang(lang('sulfur')).with_tag('minecraft:mineable/shovel')

    for crop, stages in DEFAULT_CROPS.items():
        name = 'beneath:%s' % crop if crop in ('ghost_pepper', 'gleam_flower') else 'minecraft:%s' % crop
        block = rm.blockstate(('crop', crop), variants=dict(('age=%d' % i, {'model': 'beneath:block/crop/%s_age_%d' % (crop, i)}) for i in range(stages)))
        block.with_lang(lang(crop))
        for i in range(stages):
            rm.block_model(('crop', crop + '_age_%d' % i), textures={'crop': 'beneath:block/crop/%s%d' % (crop, i)}, parent='block/crop')
        block.with_block_loot({
            'name': name,
            'conditions': loot_tables.block_state_property('beneath:crop/%s[age=%s]' % (crop, stages - 1)),
            #'functions': crop_yield(0, (6, 10)) todo: this
        }, {
            'name': 'beneath:seeds/%s' % crop
        })
        rm.item_model('seeds/%s' % crop).with_tag('tfc:seeds').with_lang(lang('%s seeds', crop))


    block = rm.blockstate_multipart('blackstone_aqueduct', *[
        {'model': 'beneath:block/blackstone_aqueduct_base'},
        ({'north': 'false'}, {'model': 'beneath:block/blackstone_aqueduct_north'}),
        ({'east': 'false'}, {'model': 'beneath:block/blackstone_aqueduct_east'}),
        ({'south': 'false'}, {'model': 'beneath:block/blackstone_aqueduct_south'}),
        ({'west': 'false'}, {'model': 'beneath:block/blackstone_aqueduct_west'}),
    ])
    block.with_lang(lang('blackstone aqueduct')).with_block_loot('beneath:blackstone_aqueduct')
    rm.item_model('blackstone_aqueduct', parent='beneath:block/blackstone_aqueduct_base', no_textures=True)
    textures = {'texture': 'minecraft:block/polished_blackstone_bricks', 'particle': 'minecraft:block/polished_blackstone_bricks'}
    rm.block_model('blackstone_aqueduct_base', textures, parent='tfc:block/aqueduct/base')
    rm.block_model('blackstone_aqueduct_north', textures, parent='tfc:block/aqueduct/north')
    rm.block_model('blackstone_aqueduct_east', textures, parent='tfc:block/aqueduct/east')
    rm.block_model('blackstone_aqueduct_south', textures, parent='tfc:block/aqueduct/south')
    rm.block_model('blackstone_aqueduct_west', textures, parent='tfc:block/aqueduct/west')

    rm.blockstate('soul_farmland').with_block_loot('minecraft:soul_soil').with_lang(lang('soul farmland')).with_block_model({'dirt': 'minecraft:block/soul_soil', 'top': 'beneath:block/soul_farmland'}, 'minecraft:block/template_farmland').with_tag('minecraft:mineable/shovel').with_item_model()

    simple_block(rm, 'cobblerack', 'minecraft:mineable/pickaxe', 'forge:cobblestone')
    simple_block(rm, 'fungal_cobblerack', 'minecraft:mineable/pickaxe')
    simple_block(rm, 'warped_thatch', 'tfc:mineable_with_sharp_tool')
    simple_block(rm, 'crimson_thatch', 'tfc:mineable_with_sharp_tool')


def simple_block(rm: ResourceManager, name: str, *tags: str) -> BlockContext:
    block = rm.blockstate(name).with_block_loot('beneath:%s' % name).with_lang(lang(name)).with_block_model().with_item_model()
    for tag in tags:
        block.with_tag(tag)
    return block

def flower_pot_cross(rm: ResourceManager, simple_name: str, name: str, model: str, texture: str, loot: str):
    rm.blockstate(name, model='beneath:block/%s' % model).with_lang(lang('potted %s', simple_name)).with_tag('minecraft:flower_pots').with_block_loot(loot, 'minecraft:flower_pot')
    rm.block_model(model, parent='minecraft:block/flower_pot_cross', textures={'plant': texture, 'dirt': 'tfc:block/dirt/loam'})

def item_model_property(rm: ResourceManager, name_parts: utils.ResourceIdentifier, overrides: utils.Json, data: Dict[str, Any]) -> ItemContext:
    res = utils.resource_location(rm.domain, name_parts)
    rm.write((*rm.resource_dir, 'assets', res.domain, 'models', 'item', res.path), {
        **data,
        'overrides': overrides
    })
    return ItemContext(rm, res)

def four_rotations(model: str, rots: Tuple[Any, Any, Any, Any], suffix: str = '', prefix: str = '') -> Dict[str, Dict[str, Any]]:
    return {
        '%sfacing=east%s' % (prefix, suffix): {'model': model, 'y': rots[0]},
        '%sfacing=north%s' % (prefix, suffix): {'model': model, 'y': rots[1]},
        '%sfacing=south%s' % (prefix, suffix): {'model': model, 'y': rots[2]},
        '%sfacing=west%s' % (prefix, suffix): {'model': model, 'y': rots[3]}
    }

def slab_loot(rm: ResourceManager, loot: str):
    return rm.block_loot(loot, {
        'name': loot,
        'functions': [{
            'function': 'minecraft:set_count',
            'conditions': [loot_tables.block_state_property(loot + '[type=double]')],
            'count': 2,
            'add': False
        }]
    })


def four_ways(model: str) -> List[Dict[str, Any]]:
    return [
        {'model': model, 'y': 90},
        {'model': model},
        {'model': model, 'y': 180},
        {'model': model, 'y': 270}
    ]
