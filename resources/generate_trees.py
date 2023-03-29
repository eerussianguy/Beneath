import os
from typing import Set, Any, Tuple, NamedTuple, Literal, Union

from nbtlib import nbt
from nbtlib.tag import String as StringTag, Int as IntTag

Tree = NamedTuple('Tree', name=str, feature=Literal['random', 'overlay', 'stacked'], variant=str, count=Union[int, Tuple[int, ...]], log=str, wood=str, leaves=str)
DATA_VERSION = 2975

TEMPLATES_DIR = 'structure_templates'
STRUCTURES_DIR = '../src/main/resources/data/beneath/structures'

NORMAL_TREES = [
    Tree('crimson', 'random', 'aspen', 16, 'beneath:wood/log/crimson', 'beneath:wood/wood/crimson', 'beneath:wood/leaves/crimson'),
    Tree('warped', 'random', 'jungle', 17, 'beneath:wood/log/warped', 'beneath:wood/wood/warped', 'beneath:wood/leaves/warped'),
]

def main():
    print('Tree sapling drop chances:')
    for tree in NORMAL_TREES:
        analyze_tree_leaves(tree)

    print('Making tree structures')
    for tree in NORMAL_TREES:
        make_tree_structures(tree)


def make_tree_structures(tree: Tree, suffix: str = ''):
    result = tree.name + suffix
    if tree.feature == 'random':
        for i in range(1, 1 + tree.count):
            make_tree_structure(tree.variant + str(i), str(i), result, tree)
    elif tree.feature == 'overlay':
        make_tree_structure(tree.variant, 'base', result, tree)
        make_tree_structure(tree.variant + '_overlay', 'overlay', result, tree)
    elif tree.feature == 'stacked':
        for j, c in zip(range(1, 1 + len(tree.count)), tree.count):
            for i in range(1, 1 + c):
                make_tree_structure('%s_layer%d_%d' % (tree.variant, j, i), 'layer%d_%d' % (j, i), result, tree)

def make_tree_structure(template: str, dest: str, wood_dir: str, tree: Tree):
    f = nbt.load('%s/%s.nbt' % (TEMPLATES_DIR, template))
    for block in f['palette']:
        if block['Name'] == 'minecraft:oak_log':
            block['Name'] = StringTag(tree.log)
            block['Properties']['natural'] = StringTag('true')
        elif block['Name'] == 'minecraft:oak_wood':
            block['Name'] = StringTag(tree.wood)
            block['Properties']['natural'] = StringTag('true')
        elif block['Name'] == 'minecraft:oak_leaves':
            block['Name'] = StringTag(tree.leaves)
            block['Properties']['persistent'] = StringTag('false')
        else:
            print('Structure: %s has an invalid block state \'%s\'' % (template, block['Name']))

    # Hack the data version, to avoid needing to run DFU on anything
    f['DataVersion'] = IntTag(DATA_VERSION)

    result_dir = '%s/%s/' % (STRUCTURES_DIR, wood_dir)
    os.makedirs(result_dir, exist_ok=True)

    file_name = result_dir + dest + '.nbt'
    try:
        if os.path.isfile(file_name):
            # Load and diff the original file - do not overwrite if source identical to avoid unnecessary git diffs due to gzip inconsistencies.
            original = nbt.load(file_name)
            if original == f:
                Count.SKIPPED += 1
                return
            else:
                Count.MODIFIED += 1
        else:
            Count.NEW += 1
        f.save(result_dir + dest + '.nbt')
    except:
        Count.ERRORS += 1

class Count:  # global mutable variables that doesn't require using the word "global" :)
    SKIPPED = 0
    NEW = 0
    MODIFIED = 0
    ERRORS = 0

def analyze_tree_leaves(tree: Tree):
    if tree.feature == 'random':
        leaves = count_leaves_in_random_tree(tree.variant, tree.count)
    elif tree.feature == 'overlay':
        leaves = count_leaves_in_overlay_tree(tree.variant)
    else:
        raise NotImplementedError

    # Base value: every tree results in 3.5 saplings, on average, if every leaf was broken
    # We bias this towards returning larger values, for larger trees, as it requires more leaves to break
    chance = 3.5 / leaves
    if chance < 0.02:
        chance = 0.2 * 0.02 + 0.8 * chance
    print('%s: %.4f,' % (repr(tree.name), chance))

def count_leaves_in_random_tree(base_name: str, count: int) -> float:
    counts = [count_leaves_in_structure(base_name + str(i)) for i in range(1, 1 + count)]
    return sum(counts) / len(counts)


def count_leaves_in_overlay_tree(base_name: str) -> float:
    base = nbt.load('%s/%s.nbt' % (TEMPLATES_DIR, base_name))
    overlay = nbt.load('%s/%s_overlay.nbt' % (TEMPLATES_DIR, base_name))

    base_leaves = leaf_ids(base)
    leaves = set(pos_key(block) for block in base['blocks'] if block['state'] in base_leaves)
    count = len(leaves)

    for block in overlay['blocks']:
        if block['state'] in base_leaves and pos_key(block) not in leaves:
            count += 0.5
        elif pos_key(block) in leaves:
            count -= 0.5

    return count

def count_leaves_in_structure(file_name: str):
    file = nbt.load('%s/%s.nbt' % (TEMPLATES_DIR, file_name))
    leaves = leaf_ids(file)
    return sum(block['state'] in leaves for block in file['blocks'])


def leaf_ids(file: nbt.File) -> Set[int]:
    return {i for i, block in enumerate(file['palette']) if block['Name'] == 'minecraft:oak_leaves'}


def pos_key(tag: Any, key: str = 'pos') -> Tuple[int, int, int]:
    pos = tag[key]
    return int(pos[0]), int(pos[1]), int(pos[2])

if __name__ == '__main__':
    main()
