from typing import Optional

from mcresources import ResourceManager, block_states, loot_tables, BlockContext, atlases
from mcresources.type_definitions import JsonObject

from constants import lang, WOODS, TREE_SAPLING_DROP_CHANCES, METALS
from data import block_and_item_tag
from recipes import damage_shapeless
from assets import four_ways, four_rotations, item_model_property, slab_loot, flower_pot_cross

def generate(rm: ResourceManager):

    # Wood Blocks
    for wood in WOODS:
        # Logs
        for variant in ('log', 'stripped_log', 'wood', 'stripped_wood'):
            block = rm.blockstate(('wood', variant, wood), variants={
                'axis=y': {'model': 'beneath:block/wood/%s/%s' % (variant, wood)},
                'axis=z': {'model': 'beneath:block/wood/%s/%s' % (variant, wood), 'x': 90},
                'axis=x': {'model': 'beneath:block/wood/%s/%s' % (variant, wood), 'x': 90, 'y': 90}
            }, use_default_model=False)

            stick_with_hammer = {
                'name': 'minecraft:stick',
                'conditions': [loot_tables.match_tag('tfc:hammers')],
                'functions': [loot_tables.set_count(1, 4)]
            }
            if variant == 'wood' or variant == 'stripped_wood':
                block.with_block_loot((
                    stick_with_hammer,
                    {  # wood blocks will only drop themselves if non-natural (aka branch_direction=none)
                        'name': 'beneath:wood/%s/%s' % (variant, wood),
                        'conditions': loot_tables.block_state_property('beneath:wood/%s/%s[branch_direction=none]' % (variant, wood))
                    },
                    'beneath:wood/%s/%s' % (variant.replace('wood', 'log'), wood)
                ))
            else:
                block.with_block_loot((
                    stick_with_hammer,
                    stick_with_hammer,
                    'beneath:wood/%s/%s' % (variant, wood)  # logs drop themselves always
                ))

            rm.item_model(('wood', variant, wood), 'beneath:item/wood/%s/%s' % (variant, wood))

            end = 'beneath:block/wood/%s/%s' % (variant.replace('log', 'log_top').replace('wood', 'log'), wood)
            side = 'beneath:block/wood/%s/%s' % (variant.replace('wood', 'log'), wood)
            block.with_block_model({'end': end, 'side': side}, parent='block/cube_column')
            if 'stripped' in variant:
                block.with_lang(lang(variant.replace('_', ' ' + wood + ' ')))
            else:
                block.with_lang(lang('%s %s', wood, variant))
        for item_type in ('lumber', 'sign', 'boat'):
            rm.item_model(('wood', item_type, wood)).with_lang(lang('%s %s', wood, item_type))
        rm.item_tag('minecraft:signs', 'beneath:wood/sign/' + wood)
        rm.item_tag('beneath:minecarts', 'beneath:wood/chest_minecart/' + wood)

        # Groundcover
        block = rm.blockstate(('wood', 'twig', wood), variants={"": four_ways('beneath:block/wood/twig/%s' % wood)}, use_default_model=False)
        block.with_lang(lang('%s twig', wood))

        block.with_block_model({'side': 'beneath:block/wood/log/%s' % wood, 'top': 'beneath:block/wood/log_top/%s' % wood}, parent='tfc:block/groundcover/twig')
        rm.item_model('wood/twig/%s' % wood, 'beneath:item/wood/twig/%s' % wood, parent='item/handheld_rod')
        block.with_block_loot('beneath:wood/twig/%s' % wood)

        rm.item_model(('wood', 'chest_minecart', wood), 'beneath:item/wood/chest_minecart_base', 'beneath:item/wood/chest_minecart_cover_%s' % wood).with_lang(lang('%s chest minecart', wood))

        block = rm.blockstate(('wood', 'fallen_leaves', wood), variants=dict((('layers=%d' % i), {'model': 'beneath:block/wood/fallen_leaves/%s_height%d' % (wood, i * 2) if i != 8 else 'beneath:block/wood/leaves/%s' % wood}) for i in range(1, 1 + 8))).with_lang(lang('fallen %s leaves', wood))
        tex = {'all': 'beneath:block/wood/leaves/%s' % wood}
        #Leaving this in in case we want to use it for other stuff
        if wood in ('mangrove', 'willow'):
            tex['top'] = 'beneath:block/wood/leaves/%s_top' % wood
        for i in range(1, 8):
            rm.block_model(('wood', 'fallen_leaves', '%s_height%s' % (wood, i * 2)), tex, parent='tfc:block/groundcover/fallen_leaves_height%s' % (i * 2))
        rm.item_model(('wood', 'fallen_leaves', wood), 'tfc:item/groundcover/fallen_leaves')
        block.with_block_loot(*[{'name': 'beneath:wood/fallen_leaves/%s' % wood, 'conditions': [loot_tables.block_state_property('beneath:wood/fallen_leaves/%s[layers=%s]' % (wood, i))], 'functions': [loot_tables.set_count(i)]} for i in range(1, 9)])


        # Leaves
        block = rm.blockstate(('wood', 'leaves', wood), model='beneath:block/wood/leaves/%s' % wood)
        block.with_block_model('beneath:block/wood/leaves/%s' % wood, parent='block/leaves')
        block.with_item_model()
        block.with_item_model()
        block.with_tag('minecraft:leaves')
        block.with_block_loot(({
               'name': 'beneath:wood/leaves/%s' % wood,
               'conditions': [loot_tables.any_of(loot_tables.match_tag('forge:shears'), loot_tables.silk_touch())]
           }, {
               'name': 'beneath:wood/sapling/%s' % wood,
               'conditions': ['minecraft:survives_explosion', loot_tables.random_chance(TREE_SAPLING_DROP_CHANCES[wood])] #Delete this bit to run for now, will fix itself when you run Generate trees.py because it will calc the sapling drop chances
           }), ({
                'name': 'minecraft:stick',
                'conditions': [loot_tables.match_tag('beneath:sharp_tools'), loot_tables.random_chance(0.2)],
                'functions': [loot_tables.set_count(1, 2)]
            }, {
                'name': 'minecraft:stick',
                'conditions': [loot_tables.random_chance(0.05)],
                'functions': [loot_tables.set_count(1, 2)]
            }))

        # Sapling
        block = rm.blockstate(('wood', 'sapling', wood), 'beneath:block/wood/sapling/%s' % wood)
        block.with_block_model({'cross': 'beneath:block/wood/sapling/%s' % wood}, 'block/cross')
        block.with_block_loot('beneath:wood/sapling/%s' % wood)
        rm.item_model(('wood', 'sapling', wood), 'beneath:block/wood/sapling/%s' % wood)

        flower_pot_cross(rm, '%s sapling' % wood, 'beneath:wood/potted_sapling/%s' % wood, 'wood/potted_sapling/%s' % wood, 'beneath:block/wood/sapling/%s' % wood, 'beneath:wood/sapling/%s' % wood)

        # Planks and variant blocks
        block = rm.block(('wood', 'planks', wood))
        block.with_blockstate()
        block.with_block_model()
        block.with_item_model()
        block.with_block_loot('beneath:wood/planks/%s' % wood)
        block.with_lang(lang('%s planks', wood))
        block.make_slab()
        block.make_stairs()
        block.make_button()
        make_door(block)
        block.make_pressure_plate()
        block.make_trapdoor()
        block.make_fence()
        block.make_fence_gate()

        for block_type in ('button', 'fence', 'fence_gate', 'pressure_plate', 'stairs', 'trapdoor'):
            rm.block_loot('wood/planks/%s_%s' % (wood, block_type), 'beneath:wood/planks/%s_%s' % (wood, block_type))
        slab_loot(rm, 'beneath:wood/planks/%s_slab' % wood)

        # Tool Rack
        rack_namespace = 'beneath:wood/planks/%s_tool_rack' % wood
        block = rm.blockstate(rack_namespace, model='beneath:block/wood/planks/%s_tool_rack' % wood, variants=four_rotations('beneath:block/wood/planks/%s_tool_rack' % wood, (270, 180, None, 90)))
        block.with_block_model(textures={'texture': 'beneath:block/wood/planks/%s' % wood, 'particle': 'beneath:block/wood/planks/%s' % wood}, parent='tfc:block/tool_rack')
        block.with_lang(lang('%s Tool Rack', wood)).with_block_loot(rack_namespace).with_item_model()

        # Loom
        block = rm.blockstate('beneath:wood/planks/%s_loom' % wood, model='beneath:block/wood/planks/%s_loom' % wood, variants=four_rotations('beneath:block/wood/planks/%s_loom' % wood, (270, 180, None, 90)))
        block.with_block_model(textures={'texture': 'beneath:block/wood/planks/%s' % wood, 'particle': 'beneath:block/wood/planks/%s' % wood}, parent='tfc:block/loom')
        block.with_item_model().with_lang(lang('%s loom', wood)).with_block_loot('beneath:wood/planks/%s_loom' % wood).with_tag('minecraft:mineable/axe')

        # Bookshelf
        slot_types = (('top_right', 2), ('bottom_mid', 4), ('top_left', 0), ('bottom_right', 5), ('bottom_left', 3), ('top_mid', 1))
        faces = (('east', 90), ('north', None), ('west', 270), ('south', 180))
        occupations = (('empty', 'false'), ('occupied', 'true'))
        shelf_mp = []
        shelf_mp += [({'facing': face}, {'model': 'beneath:block/wood/planks/%s_bookshelf' % wood, 'y': y, 'uvlock': True}) for face, y in faces]
        shelf_mp += [({'AND': [{'facing': face}, {f'slot_{i}_occupied': is_occupied}]}, {'model': f'beneath:block/wood/planks/{wood}_bookshelf_{occupation}_{slot_type}', 'y': y}) for face, y in faces for slot_type, i in slot_types for occupation, is_occupied in occupations]
        block = rm.blockstate_multipart(('wood', 'planks', '%s_bookshelf' % wood), *shelf_mp)
        rm.block_model(('wood', 'planks', '%s_bookshelf' % wood), {'top': 'beneath:block/wood/planks/%s_bookshelf_top' % wood, 'side': 'beneath:block/wood/planks/%s_bookshelf_side' % wood}, parent='minecraft:block/chiseled_bookshelf')
        block.with_lang(lang('%s bookshelf', wood)).with_block_loot('beneath:wood/planks/%s_bookshelf' % wood)
        rm.block_model(('wood', 'planks', '%s_bookshelf_inventory' % wood), {'top': 'beneath:block/wood/planks/%s_bookshelf_top' % wood, 'side': 'beneath:block/wood/planks/%s_bookshelf_side' % wood, 'front': 'beneath:block/wood/planks/%s_bookshelf_empty' % wood}, parent='minecraft:block/chiseled_bookshelf_inventory')
        rm.item_model('beneath:wood/planks/%s_bookshelf' % wood, parent='beneath:block/wood/planks/%s_bookshelf_inventory' % wood, no_textures=True)
        for slot in ('bottom_left', 'bottom_mid', 'bottom_right', 'top_left', 'top_mid', 'top_right'):
            for occupancy in ('empty', 'occupied'):
                rm.block_model(('wood', 'planks', f'{wood}_bookshelf_{occupancy}_{slot}'), {'texture': f'beneath:block/wood/planks/{wood}_bookshelf_{occupancy}'}, parent=f'minecraft:block/chiseled_bookshelf_{occupancy}_slot_{slot}')

        # Workbench
        rm.blockstate(('wood', 'planks', '%s_workbench' % wood)).with_block_model(parent='minecraft:block/cube', textures={
            'particle': 'beneath:block/wood/planks/%s_workbench_front' % wood,
            'north': 'beneath:block/wood/planks/%s_workbench_front' % wood,
            'south': 'beneath:block/wood/planks/%s_workbench_side' % wood,
            'east': 'beneath:block/wood/planks/%s_workbench_side' % wood,
            'west': 'beneath:block/wood/planks/%s_workbench_front' % wood,
            'up': 'beneath:block/wood/planks/%s_workbench_top' % wood,
            'down': 'beneath:block/wood/planks/%s' % wood
        }).with_item_model().with_lang(lang('%s Workbench', wood)).with_tag('beneath:workbenches').with_block_loot('beneath:wood/planks/%s_workbench' % wood)

        # Doors
        rm.item_model('beneath:wood/planks/%s_door' % wood, 'beneath:item/wood/planks/%s_door' % wood)
        rm.block_loot('wood/planks/%s_door' % wood, {'name': 'beneath:wood/planks/%s_door' % wood, 'conditions': [loot_tables.block_state_property('beneath:wood/planks/%s_door[half=lower]' % wood)]})

        # Log Fences
        log_fence_namespace = 'beneath:wood/planks/' + wood + '_log_fence'
        rm.blockstate_multipart(log_fence_namespace, *block_states.fence_multipart('beneath:block/wood/planks/' + wood + '_log_fence_post', 'beneath:block/wood/planks/' + wood + '_log_fence_side'))
        rm.block_model(log_fence_namespace + '_post', textures={'texture': 'beneath:block/wood/log/' + wood}, parent='block/fence_post')
        rm.block_model(log_fence_namespace + '_side', textures={'texture': 'beneath:block/wood/planks/' + wood}, parent='block/fence_side')
        rm.block_model(log_fence_namespace + '_inventory', textures={'log': 'beneath:block/wood/log/' + wood, 'planks': 'beneath:block/wood/planks/' + wood}, parent='tfc:block/log_fence_inventory')
        rm.item_model('beneath:wood/planks/' + wood + '_log_fence', parent='beneath:block/wood/planks/' + wood + '_log_fence_inventory', no_textures=True)
        rm.block_loot(log_fence_namespace, log_fence_namespace)

        texture = 'beneath:block/wood/sheet/%s' % wood
        connection = 'beneath:block/wood/support/%s_connection' % wood
        rm.blockstate_multipart(('wood', 'vertical_support', wood),
            {'model': 'beneath:block/wood/support/%s_vertical' % wood},
            ({'north': True}, {'model': connection, 'y': 270}),
            ({'east': True}, {'model': connection}),
            ({'south': True}, {'model': connection, 'y': 90}),
            ({'west': True}, {'model': connection, 'y': 180}),
        ).with_tag('beneath:support_beam').with_lang(lang('%s Support', wood)).with_block_loot('beneath:wood/support/' + wood)
        rm.blockstate_multipart(('wood', 'horizontal_support', wood),
            {'model': 'beneath:block/wood/support/%s_horizontal' % wood},
            ({'north': True}, {'model': connection, 'y': 270}),
            ({'east': True}, {'model': connection}),
            ({'south': True}, {'model': connection, 'y': 90}),
            ({'west': True}, {'model': connection, 'y': 180}),
        ).with_tag('beneath:support_beam').with_lang(lang('%s Support', wood)).with_block_loot('beneath:wood/support/' + wood)

        rm.block_model('beneath:wood/support/%s_inventory' % wood, textures={'texture': texture}, parent='tfc:block/wood/support/inventory')
        rm.block_model('beneath:wood/support/%s_vertical' % wood, textures={'texture': texture, 'particle': texture}, parent='tfc:block/wood/support/vertical')
        rm.block_model('beneath:wood/support/%s_connection' % wood, textures={'texture': texture, 'particle': texture}, parent='tfc:block/wood/support/connection')
        rm.block_model('beneath:wood/support/%s_horizontal' % wood, textures={'texture': texture, 'particle': texture}, parent='tfc:block/wood/support/horizontal')
        rm.item_model(('wood', 'support', wood), no_textures=True, parent='beneath:block/wood/support/%s_inventory' % wood).with_lang(lang('%s Support', wood))

        for chest in ('chest', 'trapped_chest'):
            rm.blockstate(('wood', chest, wood), model='beneath:block/wood/%s/%s' % (chest, wood)).with_lang(lang('%s %s', wood, chest)).with_tag('minecraft:features_cannot_replace').with_tag('minecraft:lava_pool_stone_cannot_replace')
            rm.block_model(('wood', chest, wood), textures={'particle': 'beneath:block/wood/planks/%s' % wood}, parent=None)
            rm.item_model(('wood', chest, wood), {'particle': 'beneath:block/wood/planks/%s' % wood}, parent='minecraft:item/chest')
            rm.block_loot(('wood', chest, wood), {'name': 'beneath:wood/%s/%s'%(chest,wood)})

        rm.block_model('wood/sluice/%s_upper' % wood, textures={'texture': 'beneath:block/wood/sheet/%s' % wood}, parent='tfc:block/sluice_upper')
        rm.block_model('wood/sluice/%s_lower' % wood, textures={'texture': 'beneath:block/wood/sheet/%s' % wood}, parent='tfc:block/sluice_lower')
        block = rm.blockstate(('wood', 'sluice', wood), variants={**four_rotations('beneath:block/wood/sluice/%s_upper' % wood, (90, 0, 180, 270), suffix=',upper=true'), **four_rotations('beneath:block/wood/sluice/%s_lower' % wood, (90, 0, 180, 270), suffix=',upper=false')}).with_lang(lang('%s sluice', wood))
        block.with_block_loot({'name': 'beneath:wood/sluice/%s' % wood, 'conditions': [loot_tables.block_state_property('beneath:wood/sluice/%s[upper=true]' % wood)]})
        rm.item_model(('wood', 'sluice', wood), parent='beneath:block/wood/sluice/%s_lower' % wood, no_textures=True)

        rm.block_model(('wood', 'planks', '%s_sign_particle' % wood), {'particle': 'beneath:block/wood/planks/%s' % wood}, parent=None)
        rm.blockstate(('wood', 'planks', '%s_sign' % wood), model='beneath:block/wood/planks/%s_sign' % wood).with_lang(lang('%s Sign', wood)).with_block_model({'particle': 'beneath:block/wood/planks/%s' % wood}, parent=None).with_block_loot('beneath:wood/sign/%s' % wood).with_tag('minecraft:standing_sings')
        rm.blockstate(('wood', 'planks', '%s_wall_sign' % wood), model='beneath:block/wood/planks/%s_sign' % wood).with_lang(lang('%s Sign', wood)).with_lang(lang('%s Sign', wood)).with_tag('minecraft:wall_signs')
        for metal, metal_data in METALS.items():
            if 'utility' in metal_data.types:
                for variant in ('hanging_sign', 'wall_hanging_sign'):
                    rm.blockstate(('wood', 'planks', variant, metal, wood), model='beneath:block/wood/planks/%s_sign_particle' % wood).with_lang(lang('%s %s %s', metal, wood, variant)).with_block_loot('beneath:wood/hanging_sign/%s/%s' % (metal, wood))
        for metal, metal_data in METALS.items():
            if 'utility' in metal_data.types:
                rm.item_model(('wood', 'hanging_sign', metal, wood), 'beneath:item/wood/hanging_sign_head_%s' % wood, 'tfc:item/wood/hanging_sign_head_overlay', 'tfc:item/metal/hanging_sign/%s' % metal).with_lang(lang('%s %s hanging sign', metal, wood))

        # Barrels
        texture = 'beneath:block/wood/planks/%s' % wood
        textures = {'particle': texture, 'planks': texture, 'sheet': 'beneath:block/wood/sheet/%s' % wood}

        faces = (('up', 0), ('east', 0), ('west', 180), ('south', 90), ('north', 270))
        seals = (('true', 'barrel_sealed'), ('false', 'barrel'))
        racks = (('true', '_rack'), ('false', ''))
        block = rm.blockstate(('wood', 'barrel', wood), variants=dict((
            'facing=%s,rack=%s,sealed=%s' % (face, rack, is_seal), {'model': 'beneath:block/wood/%s/%s%s%s' % (seal_type, wood, '_side' if face != 'up' else '', suffix if face != 'up' else ''), 'y': yrot if yrot != 0 else None}
        ) for face, yrot in faces for rack, suffix in racks for is_seal, seal_type in seals))

        item_model_property(rm, ('wood', 'barrel', wood), [{'predicate': {'tfc:sealed': 1.0}, 'model': 'beneath:block/wood/barrel_sealed/%s' % wood}], {'parent': 'beneath:block/wood/barrel/%s' % wood})
        block.with_block_model(textures, 'tfc:block/barrel')
        rm.block_model(('wood', 'barrel', wood + '_side'), textures, 'tfc:block/barrel_side')
        rm.block_model(('wood', 'barrel', wood + '_side_rack'), textures, 'tfc:block/barrel_side_rack')
        rm.block_model(('wood', 'barrel_sealed', wood + '_side_rack'), textures, 'tfc:block/barrel_side_sealed_rack')
        rm.block_model(('wood', 'barrel_sealed', wood), textures, 'tfc:block/barrel_sealed')
        rm.block_model(('wood', 'barrel_sealed', wood + '_side'), textures, 'tfc:block/barrel_side_sealed')
        block.with_lang(lang('%s barrel', wood))
        block.with_block_loot(({
           'name': 'beneath:wood/barrel/%s' % wood,
           'functions': [loot_tables.copy_block_entity_name(), loot_tables.copy_block_entity_nbt()],
           'conditions': [loot_tables.block_state_property('beneath:wood/barrel/%s[sealed=true]' % wood)]
        }, 'beneath:wood/barrel/%s' % wood))

        # Lecterns
        block = rm.blockstate('beneath:wood/lectern/%s' % wood, variants=four_rotations('beneath:block/wood/lectern/%s' % wood, (90, None, 180, 270)))
        block.with_block_model(textures={'bottom': 'beneath:block/wood/planks/%s' % wood, 'base': 'beneath:block/wood/lectern/%s/base' % wood, 'front': 'beneath:block/wood/lectern/%s/front' % wood, 'sides': 'beneath:block/wood/lectern/%s/sides' % wood, 'top': 'beneath:block/wood/lectern/%s/top' % wood, 'particle': 'beneath:block/wood/lectern/%s/sides' % wood}, parent='minecraft:block/lectern')
        block.with_item_model().with_lang(lang("%s lectern" % wood)).with_block_loot('beneath:wood/lectern/%s' % wood).with_tag('minecraft:mineable/axe')
        # Scribing Table
        block = rm.blockstate('beneath:wood/scribing_table/%s' % wood, variants=four_rotations('beneath:block/wood/scribing_table/%s' % wood, (90, None, 180, 270)))
        block.with_block_model(textures={'top': 'beneath:block/wood/scribing_table/%s' % wood, 'leg': 'beneath:block/wood/log/%s' % wood, 'side' : 'beneath:block/wood/planks/%s' % wood, 'misc': 'tfc:block/wood/scribing_table/scribing_paraphernalia', 'particle': 'beneath:block/wood/planks/%s' % wood}, parent='tfc:block/scribing_table')
        block.with_item_model().with_lang(lang("%s scribing table" % wood)).with_block_loot('beneath:wood/scribing_table/%s' % wood).with_tag('minecraft:mineable/axe')
        # Sewing Table
        block = rm.blockstate('wood/sewing_table/%s' % wood, variants=four_rotations('beneath:block/wood/sewing_table/%s' % wood, (90, None, 180, 270))).with_item_model()
        rm.block_model(('wood', 'sewing_table', wood), {'0': 'beneath:block/wood/log/%s' % wood, '1': 'beneath:block/wood/planks/%s' % wood}, 'tfc:block/sewing_table')
        block.with_lang(lang('%s sewing table', wood)).with_block_loot('beneath:wood/sewing_table/%s' % wood)
        # Jar shelf
        block = rm.blockstate('wood/jar_shelf/%s' % wood, variants=four_rotations('beneath:block/wood/jar_shelf/%s' % wood, (90, None, 180, 270)))
        block.with_block_model(textures={'0': 'beneath:block/wood/planks/%s' % wood}, parent='tfc:block/jar_shelf').with_item_model().with_lang(lang('%s jar shelf', wood)).with_block_loot('beneath:wood/jar_shelf/%s' % wood)

        # Axle
        block = rm.blockstate('beneath:wood/axle/%s' % wood, 'tfc:block/empty')
        block.with_lang(lang('%s axle', wood))
        block.with_block_loot('beneath:wood/axle/%s' % wood)
        block.with_block_model({'wood': 'beneath:block/wood/sheet/%s' % wood}, 'tfc:block/axle')
        rm.item_model('beneath:wood/axle/%s' % wood, no_textures=True, parent='beneath:block/wood/axle/%s' % wood)

        # Bladed Axle
        block = rm.blockstate('beneath:wood/bladed_axle/%s' % wood, 'tfc:block/empty')
        block.with_lang(lang('%s bladed axle', wood))
        block.with_block_loot('beneath:wood/bladed_axle/%s' % wood)
        block.with_block_model({'wood': 'beneath:block/wood/sheet/%s' % wood}, 'tfc:block/bladed_axle')
        rm.item_model('beneath:wood/bladed_axle/%s' % wood, no_textures=True, parent='beneath:block/wood/bladed_axle/%s' % wood)

        # Encased Axle
        block = rm.blockstate(('wood', 'encased_axle', wood), variants={
            'axis=x': {'model': 'beneath:block/wood/encased_axle/%s' % wood, 'x': 90, 'y': 90},
            'axis=y': {'model': 'beneath:block/wood/encased_axle/%s' % wood},
            'axis=z': {'model': 'beneath:block/wood/encased_axle/%s' % wood, 'x': 90},
        })
        block.with_lang(lang('%s encased axle', wood))
        block.with_block_loot('beneath:wood/encased_axle/%s' % wood)
        block.with_block_model({
            'side': 'beneath:block/wood/stripped_log/%s' % wood,
            'end': 'beneath:block/wood/planks/%s' % wood,
            'overlay': 'tfc:block/axle_casing',
            'overlay_end': 'tfc:block/axle_casing_front',
            'particle': 'beneath:block/wood/stripped_log/%s' % wood
        }, parent='tfc:block/ore_column')
        block.with_item_model()

        # Clutch
        block = rm.blockstate(('wood', 'clutch', wood), variants={
            'axis=x,powered=false': {'model': 'beneath:block/wood/clutch/%s' % wood, 'x': 90, 'y': 90},
            'axis=x,powered=true': {'model': 'beneath:block/wood/clutch/%s_powered' % wood, 'x': 90, 'y': 90},
            'axis=y,powered=false': {'model': 'beneath:block/wood/clutch/%s' % wood},
            'axis=y,powered=true': {'model': 'beneath:block/wood/clutch/%s_powered' % wood},
            'axis=z,powered=false': {'model': 'beneath:block/wood/clutch/%s' % wood, 'x': 90},
            'axis=z,powered=true': {'model': 'beneath:block/wood/clutch/%s_powered' % wood, 'x': 90},
        })
        block.with_lang(lang('%s clutch', wood))
        block.with_block_loot('beneath:wood/clutch/%s' % wood)
        block.with_block_model({
            'side': 'beneath:block/wood/stripped_log/%s' % wood,
            'end': 'beneath:block/wood/planks/%s' % wood,
            'overlay': 'tfc:block/axle_casing_unpowered',
            'overlay_end': 'tfc:block/axle_casing_front',
            'particle': 'beneath:block/wood/stripped_log/%s' % wood
        }, parent='tfc:block/ore_column')
        rm.block_model(('wood', 'clutch', '%s_powered' % wood), {
            'side': 'beneath:block/wood/stripped_log/%s' % wood,
            'end': 'beneath:block/wood/planks/%s' % wood,
            'overlay': 'tfc:block/axle_casing_powered',
            'overlay_end': 'tfc:block/axle_casing_front',
            'particle': 'beneath:block/wood/stripped_log/%s' % wood
        }, parent='tfc:block/ore_column')
        block.with_item_model()

        # Gearbox
        gearbox_port = 'beneath:block/wood/gear_box_port/%s' % wood
        gearbox_face = 'beneath:block/wood/gear_box_face/%s' % wood

        block = rm.blockstate_multipart(
            ('wood', 'gear_box', wood),
            ({'north': True}, {'model': gearbox_port}),
            ({'north': False}, {'model': gearbox_face}),
            ({'south': True}, {'model': gearbox_port, 'y': 180}),
            ({'south': False}, {'model': gearbox_face, 'y': 180}),
            ({'east': True}, {'model': gearbox_port, 'y': 90}),
            ({'east': False}, {'model': gearbox_face, 'y': 90}),
            ({'west': True}, {'model': gearbox_port, 'y': 270}),
            ({'west': False}, {'model': gearbox_face, 'y': 270}),
            ({'down': True}, {'model': gearbox_port, 'x': 90}),
            ({'down': False}, {'model': gearbox_face, 'x': 90}),
            ({'up': True}, {'model': gearbox_port, 'x': 270}),
            ({'up': False}, {'model': gearbox_face, 'x': 270}),
        )
        block.with_lang(lang('%s gear box', wood))
        block.with_block_loot('beneath:wood/gear_box/%s' % wood)

        rm.block_model(('wood', 'gear_box_port', wood), {
            'all': 'beneath:block/wood/planks/%s' % wood,
            'overlay': 'tfc:block/axle_casing_front',
        }, parent='tfc:block/gear_box_port')
        rm.block_model(('wood', 'gear_box_face', wood), {
            'all': 'beneath:block/wood/planks/%s' % wood,
            'overlay': 'tfc:block/axle_casing_round'
        }, parent='tfc:block/gear_box_face')

        rm.item_model(('wood', 'gear_box', wood), {
            'all': 'beneath:block/wood/planks/%s' % wood,
            'overlay': 'tfc:block/axle_casing_front'
        }, parent='tfc:block/ore')

        # Windmill
        block = rm.blockstate('beneath:wood/windmill/%s' % wood, 'tfc:block/empty')
        block.with_lang(lang('%s windmill', wood))
        block.with_block_loot('beneath:wood/axle/%s' % wood,)

        # Water Wheel
        block = rm.blockstate('beneath:wood/water_wheel/%s' % wood)
        block.with_block_model({'particle': 'beneath:block/wood/planks/%s' % wood}, parent=None)
        block.with_lang(lang('%s water wheel', wood))
        block.with_block_loot('beneath:wood/water_wheel/%s' % wood)
        rm.item_model('beneath:wood/water_wheel/%s' % wood, 'beneath:item/wood/water_wheel_%s' % wood)

        # Lang
        for variant in ('door', 'trapdoor', 'fence', 'log_fence', 'fence_gate', 'button', 'pressure_plate', 'slab', 'stairs'):
            rm.lang('block.beneath.wood.planks.' + wood + '_' + variant, lang('%s %s', wood, variant))
        for variant in ('sapling', 'leaves'):
            rm.lang('block.beneath.wood.' + variant + '.' + wood, lang('%s %s', wood, variant))

        rm.data(('tfc', 'supports', 'horizontal_support_beam'), {
            'ingredient': ['beneath:wood/horizontal_support/%s' % wood for wood in WOODS],
            'support_up': 2,
            'support_down': 2,
            'support_horizontal': 4
        })

        def item(_variant: str) -> str:
            return 'beneath:wood/%s/%s' % (_variant, wood)

        def plank(_variant: str) -> str:
            return 'beneath:wood/planks/%s_%s' % (wood, _variant)

        rm.item_tag('tfc:lumber', item('lumber'))
        block_and_item_tag(rm, 'tfc:twigs', item('twig'))
        block_and_item_tag(rm, 'tfc:looms', plank('loom'))
        block_and_item_tag(rm, 'tfc:sluices', item('sluice'))
        block_and_item_tag(rm, 'tfc:workbenches', plank('workbench'))
        block_and_item_tag(rm, 'tfc:bookshelves', plank('bookshelf'))
        block_and_item_tag(rm, 'tfc:lecterns', item('lectern'))
        block_and_item_tag(rm, 'tfc:barrels', item('barrel'))
        block_and_item_tag(rm, 'tfc:fallen_leaves', item('fallen_leaves'))
        block_and_item_tag(rm, 'tfc:tool_racks', plank('tool_rack'))
        rm.block_and_item_tag('scribing_tables', item('scribing_table'))
        rm.block_and_item_tag('jar_shelves', item('jar_shelf'))
        rm.block_and_item_tag('water_wheels', item('water_wheel'))
        rm.block_tag('support_beams', item('vertical_support'), item('horizontal_support'))

        rm.item_tag('axles', item('axle'), item('encased_axle'))
        rm.item_tag('gear_boxes', item('gear_box'))
        rm.item_tag('clutches', item('clutch'))
        rm.item_tag('minecraft:boats', item('boat'))
        block_and_item_tag(rm, 'minecraft:wooden_buttons', plank('button'))
        block_and_item_tag(rm, 'minecraft:wooden_fences', plank('fence'), plank('log_fence'))
        block_and_item_tag(rm, 'minecraft:wooden_slabs', plank('slab'))
        block_and_item_tag(rm, 'minecraft:wooden_stairs', plank('stairs'))
        block_and_item_tag(rm, 'minecraft:wooden_doors', plank('door'))
        block_and_item_tag(rm, 'minecraft:wooden_trapdoors', plank('trapdoor'))
        block_and_item_tag(rm, 'minecraft:wooden_pressure_plates', plank('pressure_plate'))
        block_and_item_tag(rm, 'minecraft:logs', '#tfc:%s_logs' % wood)
        block_and_item_tag(rm, 'minecraft:leaves', item('leaves'))
        block_and_item_tag(rm, 'minecraft:planks', item('planks'))
        rm.block_tag('minecraft:standing_signs', plank('sign'))
        rm.block_tag('minecraft:wall_signs', plank('wall_sign'))
        rm.item_tag('minecraft:signs', item('sign'))
        rm.item_tag('minecraft:boats', item('boat'))

        block_and_item_tag(rm, 'forge:chests/wooden', item('chest'), item('trapped_chest'))
        block_and_item_tag(rm, 'forge:fence_gates/wooden', plank('fence_gate'))
        block_and_item_tag(rm, 'forge:stripped_logs', item('stripped_log'), item('stripped_wood'))

        block_and_item_tag(rm, 'tfc:%s_logs' % wood, item('log'), item('wood'), item('stripped_log'), item('stripped_wood'))

        rm.block_tag('tfc:lit_by_dropped_torch', item('fallen_leaves'))
        rm.block_tag('tfc:converts_to_humus', item('fallen_leaves'))

        rm.block_tag('minecraft:mineable/axe', *[
            *[
                'beneath:wood/%s/%s' % (variant, wood)
                for variant in ('log', 'stripped_log', 'wood', 'stripped_wood', 'planks', 'twig', 'vertical_support', 'horizontal_support', 'sluice', 'chest', 'trapped_chest', 'barrel', 'lectern', 'scribing_table', 'jar_shelf', 'axle', 'encased_axle', 'bladed_axle', 'clutch', 'gear_box', 'windmill', 'water_wheel')
            ],
            *[
                'beneath:wood/planks/%s_%s' % (wood, variant)
                for variant in ('bookshelf', 'door', 'trapdoor', 'fence', 'log_fence', 'fence_gate', 'button', 'pressure_plate', 'slab', 'stairs', 'tool_rack', 'workbench', 'sign')
            ]])
        rm.block_tag('tfc:mineable_with_sharp_tool', *[
            *['beneath:wood/%s/%s' % (variant, wood) for variant in ('leaves', 'sapling', 'fallen_leaves')],
        ])
        rm.block_tag('tfc:mineable_with_blunt_tool', *[
            'beneath:wood/%s/%s' % (variant, wood) for variant in ('log', 'stripped_log', 'wood', 'stripped_wood')
        ])
        rm.entity_tag('tfc:destroys_floating_plants', 'beneath:boat/%s' % wood)

        log_tag = '#tfc:%s_logs' % wood


        rm.crafting_shaped('crafting/wood/%s_bookshelf' % wood, ['XXX', 'YYY', 'XXX'], {'X': item('lumber'), 'Y': '#forge:rods/wooden'}, plank('bookshelf')).with_advancement(item('lumber'))
        rm.crafting_shapeless('crafting/wood/%s_button' % wood, item('planks'), plank('button')).with_advancement(item('planks'))
        rm.crafting_shaped('crafting/wood/%s_door' % wood, ['XX', 'XX', 'XX'], {'X': item('lumber')}, (2, plank('door'))).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_fence' % wood, ['XYX', 'XYX'], {'X': item('planks'), 'Y': item('lumber')}, (8, plank('fence'))).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_log_fence' % wood, ['XYX', 'XYX'], {'X': item('log'), 'Y': item('lumber')}, (8, plank('log_fence'))).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_fence_gate' % wood, ['YXY', 'YXY'], {'X': item('planks'), 'Y': item('lumber')}, (2, plank('fence_gate'))).with_advancement(item('lumber'))
        damage_shapeless(rm, 'crafting/wood/%s_lumber_log' % wood, (log_tag, '#tfc:saws'), (8, item('lumber'))).with_advancement(item('log'))
        damage_shapeless(rm, 'crafting/wood/%s_lumber_planks' % wood, (item('planks'), '#tfc:saws'), (4, item('lumber'))).with_advancement(item('planks'))
        damage_shapeless(rm, 'crafting/wood/%s_stairs_undo' % wood, (plank('stairs'), '#tfc:saws'), (3, item('lumber'))).with_advancement(plank('stairs'))
        damage_shapeless(rm, 'crafting/wood/%s_slab_undo' % wood, (plank('slab'), '#tfc:saws'), (2, item('lumber'))).with_advancement(plank('slab'))
        rm.crafting_shaped('crafting/wood/%s_stairs' % wood, ['X  ', 'XX ', 'XXX'], {'X': item('planks')}, (8, plank('stairs'))).with_advancement(item('planks'))
        rm.crafting_shaped('crafting/wood/%s_slab' % wood, ['XXX'], {'X': item('planks')}, (6, plank('slab'))).with_advancement(item('planks'))
        rm.crafting_shaped('crafting/wood/%s_planks' % wood, ['XX', 'XX'], {'X': item('lumber')}, item('planks')).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_tool_rack' % wood, ['XXX', '   ', 'XXX'], {'X': item('lumber')}, plank('tool_rack')).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_trapdoor' % wood, ['XXX', 'XXX'], {'X': item('lumber')}, (3, plank('trapdoor'))).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_workbench' % wood, ['XX', 'XX'], {'X': item('planks')}, plank('workbench')).with_advancement(item('planks'))
        rm.crafting_shaped('crafting/wood/%s_pressure_plate' % wood, ['XX'], {'X': item('lumber')}, plank('pressure_plate')).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_boat' % wood, ['X X', 'XXX'], {'X': item('planks')}, item('boat')).with_advancement(item('planks'))
        rm.crafting_shaped('crafting/wood/%s_chest' % wood, ['XXX', 'X X', 'XXX'], {'X': item('lumber')}, item('chest')).with_advancement(item('lumber'))
        rm.crafting_shapeless('crafting/wood/%s_trapped_chest' % wood, (item('chest'), 'minecraft:tripwire_hook'), (1, item('trapped_chest'))).with_advancement(item('chest'))
        damage_shapeless(rm, 'crafting/wood/%s_support' % wood, (log_tag, log_tag, '#tfc:saws'), (8, item('support'))).with_advancement('#tfc:saws')
        rm.crafting_shaped('crafting/wood/%s_loom' % wood, ['XXX', 'XSX', 'X X'], {'X': item('lumber'), 'S': 'minecraft:stick'}, plank('loom')).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_sluice' % wood, ['  X', ' XY', 'XYY'], {'X': '#forge:rods/wooden', 'Y': item('lumber')}, item('sluice')).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_sign' % wood, ['XXX', 'XXX', ' Y '], {'X': item('lumber'), 'Y': '#forge:rods/wooden'}, (3, item('sign'))).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_barrel' % wood, ['X X', 'X X', 'XXX'], {'X': item('lumber')}, item('barrel')).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_lectern' % wood, ['XXX', ' Y ', ' X '], {'X': item('lumber'), 'Y': plank('bookshelf')}, item('lectern')).with_advancement(plank('bookshelf'))
        rm.crafting_shaped('crafting/wood/%s_scribing_table' % wood, ['F B', 'XXX', 'Y Y'], {'F': '#forge:feathers', 'B': 'minecraft:black_dye', 'X': plank('slab'), 'Y': item('planks')}, item('scribing_table')).with_advancement(item('planks'))
        rm.crafting_shaped('crafting/wood/%s_wood' % wood, ['XX', 'XX'], {'X': item('log')}, (3, item('wood'))).with_advancement(item('log'))
        rm.crafting_shapeless('crafting/wood/%s_chest_minecart' % wood, (item('chest'), 'minecraft:minecart'), item('chest_minecart'))
        rm.crafting_shaped('crafting/wood/%s_shelf' % wood, ['XXX', 'Y Y', 'Z Z'], {'X': item('planks'), 'Y': item('lumber'), 'Z': '#forge:rods/wooden'}, (2, item('jar_shelf'))).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_axle' % wood, ['WGW'], {'G': 'tfc:glue', 'W': item('stripped_log')}, (4, item('axle'))).with_advancement(item('lumber'))
        rm.crafting_shapeless('crafting/wood/%s_bladed_axle' % wood, (item('axle'), '#forge:ingots/steel'), item('bladed_axle')).with_advancement(item('axle'))
        rm.crafting_shaped('crafting/wood/%s_encased_axle' % wood, [' L ', 'WAW', ' L '], {'L': item('stripped_log'), 'W': item('lumber'), 'A': item('axle')}, (4, item('encased_axle'))).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_gear_box' % wood, [' L ', 'LML', ' L '], {'L': item('lumber'), 'M': 'tfc:brass_mechanisms'}, (2, item('gear_box'))).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_clutch' % wood, ['LSL', 'MAR', 'LSL'], {'L': item('lumber'), 'S': item('stripped_log'), 'M': 'tfc:brass_mechanisms', 'A': item('axle'), 'R': '#forge:dusts/redstone'}, (2, item('clutch'))).with_advancement(item('lumber'))
        rm.crafting_shaped('crafting/wood/%s_water_wheel' % wood, ['LPL', 'PAP', 'LPL'], {'L': item('lumber'), 'P': item('planks'), 'A': item('axle')}, item('water_wheel')).with_advancement(item('lumber'))

        for metal, metal_data in METALS.items():
            if 'utility' in metal_data.types:
                rm.crafting_shaped('crafting/wood/hanging_sign/%s/%s' % (metal, wood), ['X X', 'YYY', 'YYY'], {'X': 'tfc:metal/chain/%s' % metal, 'Y': item('lumber')}, (3, 'beneath:wood/hanging_sign/%s/%s' % (metal, wood))).with_advancement('tfc:metal/chain/%s' % metal)

        rm.atlas('minecraft:blocks',
             atlases.palette(
                 key='beneath:color_palettes/wood/planks/palette',
                 textures=['beneath:block/wood/planks/%s' % v for v in ('bookshelf_top', 'bookshelf_side')],
                 permutations=dict((wood, 'beneath:color_palettes/wood/planks/%s' % wood) for wood in WOODS)
             ),
             atlases.palette(
                 key='beneath:color_palettes/wood/planks/palette',
                 textures=['beneath:item/wood/%s' % v for v in ('twig', 'lumber', 'chest_minecart_cover', 'stripped_log', 'sign_head', 'hanging_sign_head', 'water_wheel')],
                 permutations=dict((wood, 'beneath:color_palettes/wood/plank_items/%s' % wood) for wood in WOODS)
             ),
             atlases.palette(
                 key='beneath:color_palettes/wood/planks/palette',
                 textures=['beneath:item/wood/boat'],
                 permutations=dict((wood, 'beneath:color_palettes/wood/plank_items/%s' % wood) for wood in WOODS if wood != 'palm')
             ),  # palm textures are manually done because it's a raft
         )

def make_door(block_context: BlockContext, door_suffix: str = '_door', top_texture: Optional[str] = None, bottom_texture: Optional[str] = None) -> 'BlockContext':
    """
    Generates all blockstates and models required for a standard door
    """
    door = block_context.res.join() + door_suffix
    block = block_context.res.join('block/') + door_suffix
    bottom = block + '_bottom'
    top = block + '_top'

    if top_texture is None:
        top_texture = top
    if bottom_texture is None:
        bottom_texture = bottom

    block_context.rm.blockstate(door, variants=door_blockstate(block))
    for model in ('bottom_left', 'bottom_left_open', 'bottom_right', 'bottom_right_open', 'top_left', 'top_left_open', 'top_right', 'top_right_open'):
        block_context.rm.block_model(door + '_' + model, {'top': top_texture, 'bottom': bottom_texture}, parent='block/door_%s' % model)
    block_context.rm.item_model(door)
    return block_context


def door_blockstate(base: str) -> JsonObject:
    left = base + '_bottom_left'
    left_open = base + '_bottom_left_open'
    right = base + '_bottom_right'
    right_open = base + '_bottom_right_open'
    top_left = base + '_top_left'
    top_left_open = base + '_top_left_open'
    top_right = base + '_top_right'
    top_right_open = base + '_top_right_open'
    return {
        'facing=east,half=lower,hinge=left,open=false': {'model': left},
        'facing=east,half=lower,hinge=left,open=true': {'model': left_open, 'y': 90},
        'facing=east,half=lower,hinge=right,open=false': {'model': right},
        'facing=east,half=lower,hinge=right,open=true': {'model': right_open, 'y': 270},
        'facing=east,half=upper,hinge=left,open=false': {'model': top_left},
        'facing=east,half=upper,hinge=left,open=true': {'model': top_left_open, 'y': 90},
        'facing=east,half=upper,hinge=right,open=false': {'model': top_right},
        'facing=east,half=upper,hinge=right,open=true': {'model': top_right_open, 'y': 270},
        'facing=north,half=lower,hinge=left,open=false': {'model': left, 'y': 270},
        'facing=north,half=lower,hinge=left,open=true': {'model': left_open},
        'facing=north,half=lower,hinge=right,open=false': {'model': right, 'y': 270},
        'facing=north,half=lower,hinge=right,open=true': {'model': right_open, 'y': 180},
        'facing=north,half=upper,hinge=left,open=false': {'model': top_left, 'y': 270},
        'facing=north,half=upper,hinge=left,open=true': {'model': top_left_open},
        'facing=north,half=upper,hinge=right,open=false': {'model': top_right, 'y': 270},
        'facing=north,half=upper,hinge=right,open=true': {'model': top_right_open, 'y': 180},
        'facing=south,half=lower,hinge=left,open=false': {'model': left, 'y': 90},
        'facing=south,half=lower,hinge=left,open=true': {'model': left_open, 'y': 180},
        'facing=south,half=lower,hinge=right,open=false': {'model': right, 'y': 90},
        'facing=south,half=lower,hinge=right,open=true': {'model': right_open},
        'facing=south,half=upper,hinge=left,open=false': {'model': top_left, 'y': 90},
        'facing=south,half=upper,hinge=left,open=true': {'model': top_left_open, 'y': 180},
        'facing=south,half=upper,hinge=right,open=false': {'model': top_right, 'y': 90},
        'facing=south,half=upper,hinge=right,open=true': {'model': top_right_open},
        'facing=west,half=lower,hinge=left,open=false': {'model': left, 'y': 180},
        'facing=west,half=lower,hinge=left,open=true': {'model': left_open, 'y': 270},
        'facing=west,half=lower,hinge=right,open=false': {'model': right, 'y': 180},
        'facing=west,half=lower,hinge=right,open=true': {'model': right_open, 'y': 90},
        'facing=west,half=upper,hinge=left,open=false': {'model': top_left, 'y': 180},
        'facing=west,half=upper,hinge=left,open=true': {'model': top_left_open, 'y': 270},
        'facing=west,half=upper,hinge=right,open=false': {'model': top_right, 'y': 180},
        'facing=west,half=upper,hinge=right,open=true': {'model': top_right_open, 'y': 90}
    }
