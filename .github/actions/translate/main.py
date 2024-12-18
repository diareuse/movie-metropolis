import os
import pathlib
import re
from time import sleep

import google.generativeai as genai

# === STATIC ===
QUOTA = 10  # per minute


# === STATIC ===

def set_github_action_output(output_name, output_value):
    f = open(os.path.abspath(os.environ["GITHUB_OUTPUT"]), "a")
    f.write(f'{output_name}={output_value}')
    f.close()


def translate(lang: str, path: str, model):
    segments = path.split("/")
    resdir = segments[:-2]
    values = segments[-2:]
    resdir.append(f"{values[0]}-{lang}")
    resdir.append(values[1])
    output = "/".join(resdir)
    print(f"Preparing output file:\n{output}")
    pathlib.Path(output).parent.mkdir(parents=True, exist_ok=True)

    with open(path) as f:
        content = f.read()

    intro = f"""Use the following xml code block for android resources as a source for translation. I will request translating the source to various languages, I need output of a xml code block with the same keys as in the original in the specified languages without any explanation or any other such thing. You will need to prefix character `'` with a single `\\` if it is not prefixed already or replace `'` with `&#39;`. Ensure that the first `xml` tag definition is identical to the source, including version and encoding. Optionally use a "context" attribute of a string resource to properly translate the resource to the desired language. Use the "context" property as a context where the translation is not straightforward and use it to find similar expression in the desired languages. Do not translate "context" property and do include the property in translated strings.

    ```
    {content}
    ```
    """

    print(f"Generating output… ", end="")
    response = model.generate_content(f"{intro}\ntranslate to '{lang}'")
    pattern = r"^```(?:\w+)?\s*\n(.*?)(?=^```)```"
    block: str = re.findall(pattern, response.text, re.DOTALL | re.MULTILINE)[0]

    with open(output, "w") as f:
        f.write(block.replace("'", "\\'"))

    print("DONE")


def find_strings_xml(directory):
    matching_files = []

    for root, dirs, files in os.walk(directory):
        for file in files:
            if file == "strings.xml":
                file_path = os.path.join(root, file)
                if file_path.endswith("values/strings.xml"):
                    matching_files.append(file_path)

    return matching_files


def main():
    print("Configuring Gemini… ", end="")
    genai.configure(api_key=os.environ["INPUT_GEMINIAPIKEY"])
    model = genai.GenerativeModel("gemini-1.5-flash")
    print("DONE")

    languages = ["af", "am", "ar", "as", "az", "be", "bg", "bn", "bs", "ca", "cs", "da", "de", "el", "es", "et", "eu",
                 "fa",
                 "fi", "fil", "fr", "gl", "gu", "hi", "hr", "hu", "hy", "in", "is", "it", "iw", "ja", "ka", "kk", "km",
                 "kn", "ko", "ky", "lo", "lt", "lv", "mk", "ml", "mn", "mr", "ms", "my", "nb", "ne", "nl", "or", "pa",
                 "pl",
                 "pt", "ro", "ru", "si", "sk", "sl", "sq", "sr", "sv", "sw", "ta", "te", "th", "tr", "uk", "ur", "uz",
                 "vi",
                 "zh", "zu"]

    root_path = os.environ["INPUT_PATH"]
    result = find_strings_xml(root_path if root_path else ".")

    print(f"Found {len(result)} files named 'strings.xml':")
    for path in result:
        print(path)

        print(f"""Translation will be performed for {len(languages)} languages.
    This process will take approximately {len(languages) / QUOTA} minutes""")

        for l in languages:
            translate(l, path, model)
            sleep(1 / QUOTA)


if __name__ == "__main__":
    main()
