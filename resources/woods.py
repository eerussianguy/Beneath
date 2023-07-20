from mcresources import ResourceManager, block_states, loot_tables

from constants import lang, WOODS, TREE_SAPLING_DROP_CHANCES
from data import block_and_item_tag
from recipes import damage_shapeless
from assets import four_ways, four_rotations, item_model_property, slab_loot, flower_pot_cross

def generate(rm: ResourceManager):
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
                    {  # wood blocks will only drop themselves if non-natural
                        'name': 'beneath:wood/%s/%s' % (variant, wood),
                        'conditions': loot_tables.block_state_property('beneath:wood/%s/%s[natural=false]' % (variant, wood))
                    },
                    'beneath:wood/%s/%s' % (variant.replace('wood', 'log'), wood)
                ))
            else:
                block.with_block_loot((
                    stick_with_hammer,
                    'beneath:wood/%s/%s' % (variant, wood)  # logs drop themselves always
                ))

            rm.item_model(('wood', variant, wood), 'beneath:item/wood/%s/%s' % (variant, wood))

            end = 'beneath:block/wood/%s/%s' % (variant.replace('log', 'log_top').replace('wood', 'log'), wood)
            side = 'beneath:block/wood/%s/%s' % (variant.replace('wood', 'log'), wood)
            block.with_block_model({'end': end, 'side': side}, parent='block/cube_column')
            if variant == 'stripped_log':
                block.with_lang(lang('stripped %s stem', wood))
            elif variant == 'log':
                block.with_lang(lang('%s stem', wood))
            elif variant == 'stripped_wood':
                block.with_lang(lang('stripped %s hyphae', wood))
            elif variant == 'wood':
                block.with_lang(lang('%s hyphae', wood))
        for item_type in ('lumber', 'sign', 'chest_minecart', 'boat'):
            rm.item_model(('wood', item_type, wood)).with_lang(lang('%s %s', wood, item_type))
        rm.item_tag('minecraft:signs', 'beneath:wood/sign/' + wood)
        rm.item_tag('tfc:minecarts', 'beneath:wood/chest_minecart/' + wood)

        # Groundcover
        for variant in ('twig', 'fallen_leaves'):
            block = rm.blockstate('wood/%s/%s' % (variant, wood), variants={"": four_ways('beneath:block/wood/%s/%s' % (variant, wood))}, use_default_model=False)
            block.with_lang(lang('%s %s', wood, variant)).with_tag('tfc:single_block_replaceable')

            if variant == 'twig':
                block.with_block_model({'side': 'beneath:block/wood/log/%s' % wood, 'top': 'beneath:block/wood/log_top/%s' % wood}, parent='tfc:block/groundcover/%s' % variant)
                rm.item_model('wood/%s/%s' % (variant, wood), 'beneath:item/wood/twig/%s' % wood)
                block.with_block_loot('beneath:wood/twig/%s' % wood)
            elif variant == 'fallen_leaves':
                block.with_block_model('beneath:block/wood/leaves/%s' % wood, parent='tfc:block/groundcover/%s' % variant)
                rm.item_model('wood/%s/%s' % (variant, wood), 'tfc:item/groundcover/fallen_leaves')
                block.with_block_loot('beneath:wood/%s/%s' % (variant, wood))
            else:
                block.with_item_model()

            block.with_tag('can_be_snow_piled')

        # Leaves
        block = rm.blockstate(('wood', 'leaves', wood), model='beneath:block/wood/leaves/%s' % wood)
        block.with_block_model('beneath:block/wood/leaves/%s' % wood, parent='block/leaves')
        block.with_item_model()
        block.with_tag('minecraft:leaves')
        block.with_block_loot(({
               'name': 'beneath:wood/leaves/%s' % wood,
               'conditions': [{
                   "condition": "minecraft:alternative",
                   "terms": [loot_tables.match_tag('forge:shears'), loot_tables.silk_touch()]
               }]
            }, {
               'name': 'beneath:wood/sapling/%s' % wood,
               'conditions': ['minecraft:survives_explosion', loot_tables.random_chance(TREE_SAPLING_DROP_CHANCES[wood])]
            }), ({
                'name': 'minecraft:stick',
                'conditions': [loot_tables.match_tag('tfc:sharp_tools'), loot_tables.random_chance(0.2)],
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
        block.make_door()
        block.make_pressure_plate()
        block.make_trapdoor()
        block.make_fence()
        block.make_fence_gate()

        for block_type in ('button', 'fence', 'fence_gate', 'pressure_plate', 'stairs', 'trapdoor'):
            rm.block_loot('wood/planks/%s_%s' % (wood, block_type), 'beneath:wood/planks/%s_%s' % (wood, block_type))
        slab_loot(rm, 'beneath:wood/planks/%s_slab' % wood)
        rm.block_tag('minecraft:slabs', 'beneath:wood/planks/%s_slab' % wood)

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
        block = rm.blockstate('beneath:wood/planks/%s_bookshelf' % wood, variants=dict(
            ('books_stored=%s,facing=%s' % (i, f), {'model': 'beneath:block/wood/planks/%s_bookshelf_%s' % (wood, i), 'y': r})
            for i in range(0, 7) for f, r in (('east', 90), ('north', None), ('south', 180), ('west', 270))
        ), use_default_model=False)
        for i in range(0, 7):
            rm.block_model('beneath:wood/planks/%s_bookshelf_%s' % (wood, i), parent='block/cube_column', textures={'north': 'beneath:block/wood/planks/%s_bookshelf_stage%s' % (wood, i), 'side': 'beneath:block/wood/planks/%s_bookshelf_side' % wood, 'end': 'beneath:block/wood/planks/%s_bookshelf_top' % wood})
        block.with_lang(lang('%s bookshelf', wood)).with_block_loot('beneath:wood/planks/%s_bookshelf' % wood)
        rm.item_model('beneath:wood/planks/%s_bookshelf' % wood, parent='beneath:block/wood/planks/%s_bookshelf_0' % wood, no_textures=True)

        # Workbench
        rm.blockstate(('wood', 'planks', '%s_workbench' % wood)).with_block_model(parent='minecraft:block/cube', textures={
            'particle': 'beneath:block/wood/planks/%s_workbench_front' % wood,
            'north': 'beneath:block/wood/planks/%s_workbench_front' % wood,
            'south': 'beneath:block/wood/planks/%s_workbench_side' % wood,
            'east': 'beneath:block/wood/planks/%s_workbench_side' % wood,
            'west': 'beneath:block/wood/planks/%s_workbench_front' % wood,
            'up': 'beneath:block/wood/planks/%s_workbench_top' % wood,
            'down': 'beneath:block/wood/planks/%s' % wood
        }).with_item_model().with_lang(lang('%s Workbench', wood)).with_tag('tfc:workbenches').with_block_loot('beneath:wood/planks/%s_workbench' % wood)

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
                ).with_tag('tfc:support_beams').with_lang(lang('%s Support', wood)).with_block_loot('beneath:wood/support/' + wood)
        rm.blockstate_multipart(('wood', 'horizontal_support', wood),
            {'model': 'beneath:block/wood/support/%s_horizontal' % wood},
            ({'north': True}, {'model': connection, 'y': 270}),
            ({'east': True}, {'model': connection}),
            ({'south': True}, {'model': connection, 'y': 90}),
            ({'west': True}, {'model': connection, 'y': 180}),
            ).with_tag('tfc:support_beams').with_lang(lang('%s Support', wood)).with_block_loot('beneath:wood/support/' + wood)

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

        rm.blockstate(('wood', 'planks', '%s_sign' % wood), model='beneath:block/wood/planks/%s_sign' % wood).with_lang(lang('%s Sign', wood)).with_block_model({'particle': 'beneath:block/wood/planks/%s' % wood}, parent=None).with_block_loot('beneath:wood/sign/%s' % wood).with_tag('minecraft:standing_sings')
        rm.blockstate(('wood', 'planks', '%s_wall_sign' % wood), model='beneath:block/wood/planks/%s_sign' % wood).with_lang(lang('%s Sign', wood)).with_lang(lang('%s Sign', wood)).with_tag('minecraft:wall_signs')

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
        block.with_block_model(textures={'top': 'beneath:block/wood/scribing_table/%s' % wood, 'leg': 'beneath:block/wood/log/%s' % wood, 'side': 'beneath:block/wood/planks/%s' % wood, 'misc': 'tfc:block/wood/scribing_table/scribing_paraphernalia', 'particle': 'beneath:block/wood/planks/%s' % wood}, parent='tfc:block/scribing_table')
        block.with_item_model().with_lang(lang("%s scribing table" % wood)).with_block_loot('beneath:wood/scribing_table/%s' % wood).with_tag('minecraft:mineable/axe')

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

        block_and_item_tag(rm, 'forge:chests/wooden', item('chest'), item('trapped_chest'))
        block_and_item_tag(rm, 'forge:fence_gates/wooden', plank('fence_gate'))
        block_and_item_tag(rm, 'forge:stripped_logs', item('stripped_log'), item('stripped_wood'))

        block_and_item_tag(rm, 'tfc:%s_logs' % wood, item('log'), item('wood'), item('stripped_log'), item('stripped_wood'))

        rm.block_tag('tfc:lit_by_dropped_torch', item('fallen_leaves'))
        rm.block_tag('tfc:converts_to_humus', item('fallen_leaves'))
        #if wood not in ('kapok', 'palm', 'pine', 'sequoia', 'spruce', 'white_cedar'):
        #    rm.block_tag('seasonal_leaves', item('leaves'))

        rm.block_tag('minecraft:mineable/axe', *[
            *['beneath:wood/%s/%s' % (variant, wood) for variant in ('log', 'stripped_log', 'wood', 'stripped_wood', 'planks', 'twig', 'vertical_support', 'horizontal_support', 'sluice', 'chest', 'trapped_chest')],
            *['beneath:wood/planks/%s_%s' % (wood, variant) for variant in ('bookshelf', 'door', 'trapdoor', 'fence', 'log_fence', 'fence_gate', 'button', 'pressure_plate', 'slab', 'stairs', 'tool_rack', 'workbench', 'sign')],
        ])
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


