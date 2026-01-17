from typing import Optional

from mcresources import ResourceManager, block_states, loot_tables, BlockContext, atlases
from mcresources.type_definitions import JsonObject

from constants import lang, WOODS, TREE_SAPLING_DROP_CHANCES, METALS
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

            rm.item_model(('wood', variant, wood), 'beneath:item/wood/%s/%s' % (variant, wood))

            end = 'beneath:block/wood/%s/%s' % (variant.replace('log', 'log_top').replace('wood', 'log'), wood)
            side = 'beneath:block/wood/%s/%s' % (variant.replace('wood', 'log'), wood)
            block.with_block_model({'end': end, 'side': side}, parent='block/cube_column')
            if 'stripped' in variant:
                block.with_lang(lang(variant.replace('_', ' ' + wood + ' ')))
            else:
                block.with_lang(lang('%s %s', wood, variant))

        # Signs + Hanging Signs
        rm.item_model(('wood', 'sign', wood), 'beneath:item/wood/sign/%s' % wood, 'beneath:item/wood/sign_head_%s' % wood, 'tfc:item/wood/sign_head_overlay%s' % ('_white' if wood in ('blackwood', 'willow', 'hickory') else '')).with_lang(lang('%s sign', wood))
        for metal, metal_data in METALS.items():
            if metal_data.type == 'all':
                rm.item_model(('wood', 'hanging_sign', metal, wood), 'beneath:item/wood/hanging_sign_head_%s' % wood, 'tfc:item/wood/hanging_sign_head_overlay%s' % ('_white' if wood in ('blackwood', 'willow', 'hickory') else ''), 'tfc:item/metal/hanging_sign/%s' % metal).with_lang(lang('%s %s hanging sign', metal, wood))

        rm.item_model(('wood', 'boat', wood), 'beneath:item/wood/boat_%s' % wood).with_lang(lang('%s %s', wood, ('boat' if wood != 'palm' else 'raft')))
        rm.item_model(('wood', 'lumber', wood), 'beneath:item/wood/lumber_%s' % wood).with_lang(lang('%s lumber', wood))
        rm.item_model(('wood', 'chest_minecart', wood), 'tfc:item/wood/chest_minecart_base', 'beneath:item/wood/chest_minecart_cover_%s' % wood).with_lang(lang('%s chest minecart', wood))

        # Groundcover
        block = rm.blockstate(('wood', 'twig', wood), variants={"": four_ways('beneath:block/wood/twig/%s' % wood)}, use_default_model=False)
        block.with_lang(lang('%s twig', wood))

        block.with_block_model({'side': 'beneath:block/wood/log/%s' % wood, 'top': 'beneath:block/wood/log_top/%s' % wood}, parent='tfc:block/groundcover/twig')
        rm.item_model('wood/twig/%s' % wood, 'beneath:item/wood/twig/%s' % wood, parent='item/handheld_rod')

        block = rm.blockstate(('wood', 'fallen_leaves', wood), variants=dict((('layers=%d' % i), {'model': 'beneath:block/wood/fallen_leaves/%s_height%d' % (wood, i * 2) if i != 8 else 'beneath:block/wood/leaves/%s' % wood}) for i in range(1, 1 + 8))).with_lang(lang('fallen %s leaves', wood))
        tex = {'all': 'beneath:block/wood/leaves/%s' % wood}
        #Leaving this in in case we want to use it for other stuff
        if wood in ('mangrove', 'willow'):
            tex['top'] = 'beneath:block/wood/leaves/%s_top' % wood
        for i in range(1, 8):
            rm.block_model(('wood', 'fallen_leaves', '%s_height%s' % (wood, i * 2)), tex, parent='tfc:block/groundcover/fallen_leaves_height%s' % (i * 2))
        rm.item_model(('wood', 'fallen_leaves', wood), 'tfc:item/groundcover/fallen_leaves')

        # Leaves
        block = rm.blockstate(('wood', 'leaves', wood), model='beneath:block/wood/leaves/%s' % wood)
        block.with_block_model('beneath:block/wood/leaves/%s' % wood, parent='block/leaves')
        block.with_item_model()
        block.with_item_model()

        # Sapling
        block = rm.blockstate(('wood', 'sapling', wood), 'beneath:block/wood/sapling/%s' % wood)
        block.with_block_model({'cross': 'beneath:block/wood/sapling/%s' % wood}, 'block/cross')
        rm.item_model(('wood', 'sapling', wood), 'beneath:block/wood/sapling/%s' % wood)

        flower_pot_cross(rm, '%s sapling' % wood, 'beneath:wood/potted_sapling/%s' % wood, 'wood/potted_sapling/%s' % wood, 'beneath:block/wood/sapling/%s' % wood, 'beneath:wood/sapling/%s' % wood)

        # Planks and variant blocks
        block = rm.block(('wood', 'planks', wood))
        block.with_blockstate()
        block.with_block_model()
        block.with_item_model()
        block.with_lang(lang('%s planks', wood))

        # Slabs, Stairs
        # N.B. These use the naming convention of `tfc:wood/planks/<wood>_<slab>`. This is for general consistency with
        # how we label slabs and stairs across the mod, and also to indicate that these are slabs of wood planks
        block.make_slab()
        block.make_stairs()

        # Pressure Plate
        block = rm.block('wood/pressure_plate/%s' % wood)
        block.make_pressure_plate('', 'beneath:block/wood/planks/%s' % wood)
        block.with_lang(lang('%s pressure plate', wood))

        # Button
        block = rm.block('wood/button/%s' % wood)
        block.make_button('', 'beneath:block/wood/planks/%s' % wood)
        block.with_lang(lang('%s button', wood))

        # Tool Rack
        block = rm.blockstate('beneath:wood/tool_rack/%s' % wood, model='beneath:block/wood/tool_rack/%s' % wood, variants=four_rotations('beneath:block/wood/tool_rack/%s' % wood, (270, 180, None, 90)))
        block.with_block_model(textures={
            'texture': 'beneath:block/wood/planks/%s' % wood,
            'particle': 'beneath:block/wood/planks/%s' % wood
        }, parent='tfc:block/tool_rack')
        block.with_lang(lang('%s Tool Rack', wood))
        block.with_item_model()

        # Loom
        block = rm.blockstate('beneath:wood/loom/%s' % wood, model='beneath:block/wood/loom/%s' % wood, variants=four_rotations('beneath:block/wood/loom/%s' % wood, (270, 180, None, 90)))
        block.with_block_model(textures={
            'texture': 'beneath:block/wood/planks/%s' % wood,
            'particle': 'beneath:block/wood/planks/%s' % wood
        }, parent='tfc:block/loom')
        block.with_item_model()
        block.with_lang(lang('%s loom', wood))

        # Bookshelf
        faces = (('east', 90), ('north', None), ('west', 270), ('south', 180))
        parts = [
                    ({'facing': face}, {'model': 'beneath:block/wood/bookshelf/%s' % wood, 'y': y, 'uvlock': True})
                    for face, y in faces
                ] + [
                    ({'AND': [{'facing': face}, {f'slot_{i}_occupied': is_occupied}]}, {'model': f'beneath:block/wood/bookshelf/{wood}_{occupation}_{slot_type}', 'y': y})
                    for face, y in faces
                    for slot_type, i in (('top_right', 2), ('bottom_mid', 4), ('top_left', 0), ('bottom_right', 5), ('bottom_left', 3), ('top_mid', 1))
                    for occupation, is_occupied in (('empty', 'false'), ('occupied', 'true'))
                ]

        block = rm.blockstate_multipart(('wood', 'bookshelf', wood), *parts)
        block.with_lang(lang('%s bookshelf', wood))
        rm.block_model(('wood', 'bookshelf', wood), {
            'top': 'beneath:block/wood/bookshelf/top_%s' % wood,
            'side': 'beneath:block/wood/bookshelf/side_%s' % wood
        }, parent='minecraft:block/chiseled_bookshelf')
        rm.block_model('wood/bookshelf/%s_inventory' % wood, {
            'top': 'beneath:block/wood/bookshelf/top_%s' % wood,
            'side': 'beneath:block/wood/bookshelf/side_%s' % wood,
            'front': 'beneath:block/wood/bookshelf/%s_empty' % wood
        }, parent='minecraft:block/chiseled_bookshelf_inventory')
        rm.item_model('beneath:wood/bookshelf/%s' % wood, parent='beneath:block/wood/bookshelf/%s_inventory' % wood, no_textures=True)

        for slot in ('bottom_left', 'bottom_mid', 'bottom_right', 'top_left', 'top_mid', 'top_right'):
            for occupancy in ('empty', 'occupied'):
                rm.block_model(f'wood/bookshelf/{wood}_{occupancy}_{slot}', {
                    'texture': f'beneath:block/wood/bookshelf/{wood}_{occupancy}'
                }, parent=f'minecraft:block/chiseled_bookshelf_{occupancy}_slot_{slot}')

        # Workbench
        block = rm.blockstate(('wood', 'workbench', wood)).with_block_model(parent='minecraft:block/cube', textures={
            'particle': 'beneath:block/wood/workbench/%s_front' % wood,
            'north': 'beneath:block/wood/workbench/%s_front' % wood,
            'south': 'beneath:block/wood/workbench/%s_side' % wood,
            'east': 'beneath:block/wood/workbench/%s_side' % wood,
            'west': 'beneath:block/wood/workbench/%s_front' % wood,
            'up': 'beneath:block/wood/workbench/%s_top' % wood,
            'down': 'beneath:block/wood/planks/%s' % wood
        })
        block.with_item_model()
        block.with_lang(lang('%s Workbench', wood))

        # Doors
        block = rm.blockstate('wood/door/%s' % wood, variants=door_blockstate('beneath:block/wood/door/%s' % wood))
        rm.item_model('beneath:wood/door/%s' % wood, 'beneath:item/wood/door/%s' % wood)
        block.with_lang(lang('%s door', wood))

        for model in ('bottom_left', 'bottom_left_open', 'bottom_right', 'bottom_right_open', 'top_left', 'top_left_open', 'top_right', 'top_right_open'):
            rm.block_model('beneath:wood/door/%s_%s' % (wood, model), {
                'top': 'beneath:block/wood/door/%s_top' % wood,
                'bottom': 'beneath:block/wood/door/%s_bottom' % wood
            }, parent='block/door_%s' % model)

        # Trapdoor
        block = rm.block('wood/trapdoor/%s' % wood)
        block.make_trapdoor('', 'beneath:block/wood/trapdoor/%s' % wood)
        block.with_lang(lang('%s trapdoor', wood))

        # Fences, Log Fences, Fence Gates
        block = rm.block('wood/fence/%s' % wood)
        block.make_fence('', 'beneath:block/wood/planks/%s' % wood)
        block.with_lang(lang('%s fence', wood))

        block = rm.block('wood/fence_gate/%s' % wood)
        block.make_fence_gate('', 'beneath:block/wood/planks/%s' % wood)
        block.with_lang(lang('%s fence gate', wood))

        # Log Fences - need to copy `make_fence()` because we have separate textures for post and side
        block = rm.blockstate_multipart('wood/log_fence/%s' % wood, *block_states.fence_multipart('beneath:block/wood/log_fence/%s_post' % wood, 'beneath:block/wood/log_fence/%s_side' % wood))
        block.with_lang(lang('%s log fence', wood))
        rm.block_model('wood/log_fence/%s_post' % wood, textures={'texture': 'beneath:block/wood/log/' + wood}, parent='block/fence_post')
        rm.block_model('wood/log_fence/%s_side' % wood, textures={'texture': 'beneath:block/wood/planks/' + wood}, parent='block/fence_side')
        rm.block_model('wood/log_fence/%s_inventory' % wood, textures={
            'log': 'beneath:block/wood/log/' + wood,
            'planks': 'beneath:block/wood/planks/' + wood
        }, parent='tfc:block/wood/log_fence/inventory')
        rm.item_model('wood/log_fence/%s' % wood, parent='beneath:block/wood/log_fence/%s_inventory' % wood, no_textures=True)

        texture = 'beneath:block/wood/sheet/%s' % wood
        connection = 'beneath:block/wood/support/%s_connection' % wood
        rm.blockstate_multipart(('wood', 'vertical_support', wood),
            {'model': 'beneath:block/wood/support/%s_vertical' % wood},
            ({'north': True}, {'model': connection, 'y': 270}),
            ({'east': True}, {'model': connection}),
            ({'south': True}, {'model': connection, 'y': 90}),
            ({'west': True}, {'model': connection, 'y': 180}),
        ).with_lang(lang('%s Support', wood))
        rm.blockstate_multipart(('wood', 'horizontal_support', wood),
            {'model': 'beneath:block/wood/support/%s_horizontal' % wood},
            ({'north': True}, {'model': connection, 'y': 270}),
            ({'east': True}, {'model': connection}),
            ({'south': True}, {'model': connection, 'y': 90}),
            ({'west': True}, {'model': connection, 'y': 180}),
        ).with_lang(lang('%s Support', wood))

        rm.block_model('beneath:wood/support/%s_inventory' % wood, textures={'texture': texture}, parent='tfc:block/wood/support/inventory')
        rm.block_model('beneath:wood/support/%s_vertical' % wood, textures={'texture': texture, 'particle': texture}, parent='tfc:block/wood/support/vertical')
        rm.block_model('beneath:wood/support/%s_connection' % wood, textures={'texture': texture, 'particle': texture}, parent='tfc:block/wood/support/connection')
        rm.block_model('beneath:wood/support/%s_horizontal' % wood, textures={'texture': texture, 'particle': texture}, parent='tfc:block/wood/support/horizontal')
        rm.item_model(('wood', 'support', wood), no_textures=True, parent='beneath:block/wood/support/%s_inventory' % wood).with_lang(lang('%s Support', wood))

        for chest in ('chest', 'trapped_chest'):
            rm.blockstate(('wood', chest, wood), model='beneath:block/wood/%s/%s' % (chest, wood)).with_lang(lang('%s %s', wood, chest))
            rm.block_model(('wood', chest, wood), textures={'particle': 'beneath:block/wood/planks/%s' % wood}, parent=None)
            rm.item_model(('wood', chest, wood), {'particle': 'beneath:block/wood/planks/%s' % wood}, parent='minecraft:item/chest')

        rm.block_model('wood/sluice/%s_upper' % wood, textures={'texture': 'beneath:block/wood/sheet/%s' % wood}, parent='tfc:block/sluice_upper')
        rm.block_model('wood/sluice/%s_lower' % wood, textures={'texture': 'beneath:block/wood/sheet/%s' % wood}, parent='tfc:block/sluice_lower')
        block = rm.blockstate(('wood', 'sluice', wood), variants={**four_rotations('beneath:block/wood/sluice/%s_upper' % wood, (90, 0, 180, 270), suffix=',upper=true'), **four_rotations('beneath:block/wood/sluice/%s_lower' % wood, (90, 0, 180, 270), suffix=',upper=false')}).with_lang(lang('%s sluice', wood))
        rm.item_model(('wood', 'sluice', wood), parent='beneath:block/wood/sluice/%s_lower' % wood, no_textures=True)

        # Signs / Hanging Signs
        rm.block_model('wood/sign/%s_particle' % wood, {
            'particle': 'beneath:block/wood/planks/%s' % wood
        }, parent=None)

        for variant in ('sign', 'wall_sign'):
            block = rm.blockstate(('wood', variant, wood), model='beneath:block/wood/sign/%s_particle' % wood)
            block.with_lang(lang('%s %s', wood, variant))

        for metal, metal_data in METALS.items():
            if metal_data.type == 'all':
                for variant in ('hanging_sign', 'wall_hanging_sign'):
                    block = rm.blockstate(('wood', variant, metal, wood), model='beneath:block/wood/sign/%s_particle' % wood)
                    block.with_lang(lang('%s %s %s', metal, wood, variant))

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

        # Lecterns
        block = rm.blockstate('beneath:wood/lectern/%s' % wood, variants=four_rotations('beneath:block/wood/lectern/%s' % wood, (90, None, 180, 270)))
        block.with_block_model(textures={'bottom': 'beneath:block/wood/planks/%s' % wood, 'base': 'beneath:block/wood/lectern/%s/base' % wood, 'front': 'beneath:block/wood/lectern/%s/front' % wood, 'sides': 'beneath:block/wood/lectern/%s/sides' % wood, 'top': 'beneath:block/wood/lectern/%s/top' % wood, 'particle': 'beneath:block/wood/lectern/%s/sides' % wood}, parent='minecraft:block/lectern')
        block.with_item_model().with_lang(lang("%s lectern" % wood))
        # Scribing Table
        block = rm.blockstate('beneath:wood/scribing_table/%s' % wood, variants=four_rotations('beneath:block/wood/scribing_table/%s' % wood, (90, None, 180, 270)))
        block.with_block_model(textures={'top': 'beneath:block/wood/scribing_table/%s' % wood, 'leg': 'beneath:block/wood/log/%s' % wood, 'side' : 'beneath:block/wood/planks/%s' % wood, 'misc': 'tfc:block/wood/scribing_table/scribing_paraphernalia', 'particle': 'beneath:block/wood/planks/%s' % wood}, parent='tfc:block/scribing_table')
        block.with_item_model().with_lang(lang("%s scribing table" % wood))
        # Sewing Table
        block = rm.blockstate('wood/sewing_table/%s' % wood, variants=four_rotations('beneath:block/wood/sewing_table/%s' % wood, (90, None, 180, 270))).with_item_model()
        rm.block_model(('wood', 'sewing_table', wood), {'0': 'beneath:block/wood/log/%s' % wood, '1': 'beneath:block/wood/planks/%s' % wood}, 'tfc:block/sewing_table')
        block.with_lang(lang('%s sewing table', wood))
        # Shelf
        block = rm.blockstate('wood/shelf/%s' % wood, variants=four_rotations('beneath:block/wood/shelf/%s' % wood, (90, None, 180, 270)))
        block.with_block_model(textures={
            '0': 'beneath:block/wood/planks/%s' % wood
        }, parent='tfc:block/wood/shelf')
        block.with_item_model()
        block.with_lang(lang('%s shelf', wood))
        # Axle
        block = rm.blockstate('beneath:wood/axle/%s' % wood, 'tfc:block/empty')
        block.with_lang(lang('%s axle', wood))
        block.with_block_model({'wood': 'beneath:block/wood/sheet/%s' % wood}, 'tfc:block/axle')
        rm.item_model('beneath:wood/axle/%s' % wood, no_textures=True, parent='beneath:block/wood/axle/%s' % wood)

        # Bladed Axle
        block = rm.blockstate('beneath:wood/bladed_axle/%s' % wood, 'tfc:block/empty')
        block.with_lang(lang('%s bladed axle', wood))
        block.with_block_model({'wood': 'beneath:block/wood/sheet/%s' % wood}, 'tfc:block/bladed_axle')
        rm.item_model('beneath:wood/bladed_axle/%s' % wood, no_textures=True, parent='beneath:block/wood/bladed_axle/%s' % wood)

        # Encased Axle
        block = rm.blockstate(('wood', 'encased_axle', wood), variants={
            'axis=x': {'model': 'beneath:block/wood/encased_axle/%s' % wood, 'x': 90, 'y': 90},
            'axis=y': {'model': 'beneath:block/wood/encased_axle/%s' % wood},
            'axis=z': {'model': 'beneath:block/wood/encased_axle/%s' % wood, 'x': 90},
        })
        block.with_lang(lang('%s encased axle', wood))
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

        # Water Wheel
        block = rm.blockstate('beneath:wood/water_wheel/%s' % wood)
        block.with_block_model({'particle': 'beneath:block/wood/planks/%s' % wood}, parent=None)
        block.with_lang(lang('%s water wheel', wood))
        rm.item_model('beneath:wood/water_wheel/%s' % wood, 'beneath:item/wood/water_wheel_%s' % wood)

        # Lang
        for variant in ('slab', 'stairs'):
            rm.lang('block.beneath.wood.planks.' + wood + '_' + variant, lang('%s %s', wood, variant))
        for variant in ('door', 'trapdoor', 'fence', 'log_fence', 'fence_gate', 'button', 'pressure_plate', 'sapling', 'leaves'):
            rm.lang('block.beneath.wood.' + variant + '.' + wood, lang('%s %s', wood, variant))

        rm.atlas('minecraft:blocks',
             atlases.palette(
                 key='beneath:color_palettes/wood/planks/palette',
                 textures=['beneath:block/wood/bookshelf/%s' % v for v in ('top', 'side')],
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
