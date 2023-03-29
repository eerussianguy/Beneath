import os
from typing import Set, Any, Tuple, NamedTuple, Literal, Union
from glob import glob

from nbtlib import nbt
from nbtlib.tag import String as StringTag, Int as IntTag

TEMPLATES_DIR = 'structure_templates'
STRUCTURES_DIR = '../src/main/resources/data/beneath/structures'
STRUCTURES_DIR_MC = '../src/main/resources/data/minecraft/structures'

def main():
    #bastion_structures = glob(TEMPLATES_DIR + '/bastion/**/*.nbt', recursive=True)
    #find_chests(bastion_structures)

    portal_structures = glob(TEMPLATES_DIR + '/ruined_portal/*.nbt')
    find_chests(portal_structures)
    fix_stone_bricks(portal_structures)
    pass


# What this does is modify the structure NBT to use our chests
# I don't host the vanilla structure files in the templates folder as that is bad practice
# But if these need to be changed, just copy structures/THE_STRUCTURE into the templates directory, and modify it
def find_chests(structures):
    crimson = True
    doubles_crimson = True
    for f in structures:
        the_nbt = nbt.load(f)
        dirty = False
        for block in the_nbt['palette']:
            if block['Name'] == 'minecraft:chest':
                dirty = True
                if 'Properties' in block and 'type' in block['Properties'] and block['Properties']['type'] != 'single':
                    block['Name'] = StringTag('beneath:wood/chest/crimson') if doubles_crimson else StringTag('beneath:wood/chest/warped')
                    print("detected double chest: crimson: " + str(doubles_crimson))
                else:
                    block['Name'] = StringTag('beneath:wood/chest/crimson') if crimson else StringTag('beneath:wood/chest/warped')
                    crimson = not crimson
        if dirty:
            save(the_nbt, f)
            doubles_crimson = not doubles_crimson

def fix_stone_bricks(structures):
    rocks = ['basalt', 'chert', 'andesite', 'marble', 'dolomite']
    i = 0
    for f in structures:
        the_nbt = nbt.load(f)
        dirty = False
        rock = rocks[i]
        for block in the_nbt['palette']:
            name = block['Name']
            if name == 'minecraft:stone_bricks':
                block['Name'] = StringTag('tfc:rock/bricks/%s' % rock)
                dirty = True
            elif name == 'minecraft:stone_brick_slab':
                block['Name'] = StringTag('tfc:rock/bricks/%s_slab' % rock)
                dirty = True
            elif name == 'minecraft:stone_brick_stairs':
                block['Name'] = StringTag('tfc:rock/bricks/%s_stairs' % rock)
                dirty = True
            elif name == 'minecraft:chiseled_stone_bricks':
                block['Name'] = StringTag('tfc:rock/chiseled/%s' % rock)
                dirty = True
        if dirty:
            save(the_nbt, f)
            i = 0 if i + 1 == len(rocks) else i + 1


def save(the_nbt, f):
    result_dir = STRUCTURES_DIR_MC + f.replace('structure_templates', '')
    result_folder = os.path.dirname(result_dir)
    os.makedirs(result_folder, exist_ok=True)
    the_nbt.save(result_dir)

if __name__ == '__main__':
    main()