import format_lang
from patchouli import *
from argparse import ArgumentParser
from typing import Optional

BOOK_LANGUAGES = ('en_us',)
MOD_LANGUAGES = ('en_us',)

class LocalInstance:
    INSTANCE_DIR = None

    @staticmethod
    def wrap(rm: ResourceManager):
        def data(name_parts: ResourceIdentifier, data_in: JsonObject, root_domain: str = 'data'):
            return rm.write((LocalInstance.INSTANCE_DIR, '/'.join(utils.str_path(name_parts))), data_in)

        if LocalInstance.INSTANCE_DIR is not None:
            rm.data = data
            return rm
        return None

def main_with_args():
    parser = ArgumentParser('generate_book.py')
    parser.add_argument('--translate', type=str, default='en_us', help='The language to translate to')
    parser.add_argument('--local', type=str, default=None, help='The directory of a local .minecraft to copy into')
    parser.add_argument('--translate-all', type=str, default=None, help='If all languages should be translated')
    parser.add_argument('--format', type=str, default=None, help='Format the mod languages')
    parser.add_argument('--reverse-translate', type=str, default=None, help='Reverse a translation from the mod files.')

    args = parser.parse_args()

    if args.format:
        do_format()
        return

    if args.translate_all:
        do_format()
        for la in BOOK_LANGUAGES:
            main(la, args.local, False, reverse_translate=args.reverse_translate is not None)
    else:
        main(args.translate, args.local, False, reverse_translate=args.reverse_translate is not None)

def do_format():
    # format_lang.main(False, 'minecraft', BOOK_LANGUAGES)
    format_lang.main(False, 'beneath', MOD_LANGUAGES)

def main(translate_lang: str, local_minecraft_dir: Optional[str], validate: bool, validating_rm: ResourceManager = None, reverse_translate: bool = False):
    LocalInstance.INSTANCE_DIR = local_minecraft_dir

    rm = ResourceManager('tfc', './src/main/resources')
    if validate:
        rm = validating_rm
    i18n = I18n(translate_lang, validate)

    print('Writing book at %s' % translate_lang)
    make_book(rm, i18n, local_instance=False, reverse_translate=reverse_translate)

    i18n.flush()

    if LocalInstance.wrap(rm):
        print('Copying %s book into local instance at: %s' % (translate_lang, LocalInstance.INSTANCE_DIR))
        make_book(rm, I18n(translate_lang, validate), local_instance=True)


# def main():
#     for language in BOOK_LANGUAGES:
#         rm = ResourceManager('tfc', '../src/main/resources')
#         i18n = I18n.create(language)
#
#         print('Writing book %s' % language)
#         make_book(rm, i18n)
#
#         i18n.flush()
#
#         if LocalInstance.wrap(rm) and language == 'en_us':
#             print('Copying into local instance at: %s' % LocalInstance.INSTANCE_DIR)
#             make_book(rm, I18n.create('en_us'), local_instance=True)
#
#         print('Done')

def make_book(rm: ResourceManager, i18n: I18n, local_instance: bool = False, reverse_translate: bool = False):
    book = Book(rm, 'field_guide', {}, i18n, local_instance, reverse_translate)

    book.category('beneath', 'Beneath', 'All about what is Beneath', 'beneath:cursecoal', is_sorted=True, entries=(
        entry('beneath', 'What Lies Beneath', 'beneath:textures/item/cursecoal.png', pages=(
            text('XXX'),
            empty_last_page()
        )),
    ))

    book.build()

# beneath Pages

def knapping(recipe: str, text_content: TranslatableStr) -> Page: return recipe_page('knapping_recipe', recipe, text_content)

def alloy_recipe(title: str, ingot: str, *components: Tuple[str, int, int], text_content: str) -> Page:
    recipe = ''.join(['$(li)%d - %d %% : $(thing)%s$()' % (lo, hi, alloy) for (alloy, lo, hi) in components])
    return item_spotlight(ingot, title, False, '$(br)$(bold)Requirements:$()$(br)' + recipe + '$(br2)' + text_content)

def custom_component(x: int, y: int, class_name: str, data: JsonObject) -> Component:
    return Component('patchouli:custom', x, y, {'class': 'com.eerussianguy.beneath.compat.patchouli.' + class_name, **data})


if __name__ == '__main__':
    main_with_args()

