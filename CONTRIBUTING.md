# Contributing to Dimmer project

:+1::tada: First off, thanks for taking the time to contribute! :tada::+1:

The following is a set of guidelines for contributing to Dimmer project which is hosted on GitHub. These are mostly guidelines, not rules. Use your best judgment, and feel free to propose changes to this document in a pull request.

## Table Of Contents



## Code of Conduct

This project and everyone participating in it is governed by the [Dimmer Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to [dev@cloudyrock.io][dev_email].

## It's just a question, I don't want to read this whole thing!!!

> **Note:** Please don't file an issue to ask a question.
 
We have an official [FAQ page](FAQ.md) where your question may be already answered. Otherwise an email to [dev@cloudyrock][dev_email]  will be the faster way to get tour question resolved. 
 
## Dimmer project packages
Dimmer project contains 4 main areas. 
- **Core:** Base project for the basic Dimmer module and Dimmer server. Issue label is `CORE-MODULE`
- **Dimmer:** Basic Dimmer module to used locally or as a client. Issue label is `LOCAL-MODULE`
- **Dimmer server:** Dimmer as a centralised server. Issue label is `SERVER-MODULE`
- **Samples:** Set of examples using Dimmer project.  Issue label is `SAMPLES-MODULE`
 
## How Can I Contribute?

### Reporting Bugs

This section guides you through submitting a bug report for Dimmer project. 

Before creating bug reports, please check [this list](#before-submitting-a-bug-report) as you might find out that you don't need to create one. When you are creating a bug report, please [include as many details as possible](#how-do-i-submit-a-good-bug-report). Fill out [the required template](ISSUE_TEMPLATE.md), the information it asks for helps us resolve issues faster.

> **Note:** If you find a **Closed** issue that seems like it is the same thing that you're experiencing, open a new issue and include a link to the original issue in the body of your new one.

#### Before Submitting A Bug Report

* **Check the [FAQ page](FAQ.md)** for a list of common questions and problems.
* **Determine [which module the problem should be reported in](#dimmer-project-packages)**.
* **Perform a [cursory search](https://github.com/issues?utf8=%E2%9C%93&q=is%3Aissue+archived%3Afalse+repo%3Acloudyrock%2Fdimmer-project+)** to see if the problem has already been reported. If it has **and the issue is still open**, add a comment to the existing issue instead of opening a new one.

#### How Do I Submit A (Good) Bug Report?

Bugs are tracked as [GitHub issues](https://guides.github.com/features/issues/). Create an issue on the main repository referencing the specific module(by the right label) and provide the required information by filling in [the template](ISSUE_TEMPLATE.md).

Please use the `bug` label and the module label for the issue. For instance, a bug in dimmer local should contain, at least, the label `bug` and `LOCAL-MODULE`.

### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion for Dimmer project, including completely new features and minor improvements to existing functionality.

#### Before Submitting An Enhancement Suggestion


* **Check the [FAQ page](FAQ.md)**. You might find out that enhancement is already covered.
* **Determine [which module the enhancement should be reported in](#dimmer-project-packages)**.
* **Perform a [cursory search](https://github.com/issues?utf8=%E2%9C%93&q=is%3Aissue+archived%3Afalse+repo%3Acloudyrock%2Fdimmer-project+)** to see if the problem has already been reported. If it has **and the issue is still open**, add a comment to the existing issue instead of opening a new one.


#### How Do I Submit A (Good) Enhancement Suggestion?

Enhancement suggestions are tracked as [GitHub issues](https://guides.github.com/features/issues/). Create an enhancement on the main repository and provide the required information  by filling in [the template](ENHANCEMENT_TEMPLATE.md).

Please use at least the module label for the enhancement. For instance, a enhancement in dimmer local should contain, at least, `LOCAL-MODULE`.

In addition, if you believe you have the knowledge to evaluate the issue complexity, you could add one of these labels: `beginner`,`intermediate`, `advance`

### Your First Code Contribution

Unsure where to begin contributing to Atom? You can start by looking through these `beginner` and `intermediate` issues:

* [Beginner issues](https://github.com/cloudyrock/dimmer-project/issues?q=is%3Aopen+is%3Aissue+label%3Abeginner) - issues which should only require a few lines of code, and a test or two.
* [Intermediate issues](https://github.com/cloudyrock/dimmer-project/issues?q=is%3Aopen+is%3Aissue+label%3Aadvance) - issues which should be a bit more involved than `beginner` issues.





















### Code style
- No comments, only when it's very complex
- finals

### Test
- Keep test coverage
- UTest and ITest
- Unit and integrations tests
- test names convention 


### Java docs
When affects to API

### steps
- fork repository
- do the change
    - code
    - tests
    - Java docs
    - Update readme
- Pull request
- CI and sonar check




### Pull Requests

* Fill in [the required template](PULL_REQUEST_TEMPLATE.md)
* Do not include issue numbers in the PR title
* Include screenshots and animated GIFs in your pull request whenever possible.
* Follow the [JavaScript](#javascript-styleguide) and [CoffeeScript](#coffeescript-styleguide) styleguides.
* Include thoughtfully-worded, well-structured [Jasmine](https://jasmine.github.io/) specs in the `./spec` folder. Run them using `atom --test spec`. See the [Specs Styleguide](#specs-styleguide) below.
* Document new code based on the [Documentation Styleguide](#documentation-styleguide)
* End all files with a newline
* [Avoid platform-dependent code](https://flight-manual.atom.io/hacking-atom/sections/cross-platform-compatibility/)
* Place requires in the following order:
    * Built in Node Modules (such as `path`)
    * Built in Atom and Electron Modules (such as `atom`, `remote`)
    * Local Modules (using relative paths)
* Place class properties in the following order:
    * Class methods and properties (methods starting with a `@` in CoffeeScript or `static` in JavaScript)
    * Instance methods and properties

## Styleguides

### Git Commit Messages

* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
* Limit the first line to 72 characters or less
* Reference issues and pull requests liberally after the first line
* When only changing documentation, include `[ci skip]` in the commit title
* Consider starting the commit message with an applicable emoji:
    * :art: `:art:` when improving the format/structure of the code
    * :racehorse: `:racehorse:` when improving performance
    * :non-potable_water: `:non-potable_water:` when plugging memory leaks
    * :memo: `:memo:` when writing docs
    * :penguin: `:penguin:` when fixing something on Linux
    * :apple: `:apple:` when fixing something on macOS
    * :checkered_flag: `:checkered_flag:` when fixing something on Windows
    * :bug: `:bug:` when fixing a bug
    * :fire: `:fire:` when removing code or files
    * :green_heart: `:green_heart:` when fixing the CI build
    * :white_check_mark: `:white_check_mark:` when adding tests
    * :lock: `:lock:` when dealing with security
    * :arrow_up: `:arrow_up:` when upgrading dependencies
    * :arrow_down: `:arrow_down:` when downgrading dependencies
    * :shirt: `:shirt:` when removing linter warnings

### JavaScript Styleguide

All JavaScript must adhere to [JavaScript Standard Style](https://standardjs.com/).

* Prefer the object spread operator (`{...anotherObj}`) to `Object.assign()`
* Inline `export`s with expressions whenever possible
  ```js
  // Use this:
  export default class ClassName {

  }

  // Instead of:
  class ClassName {

  }
  export default ClassName
  ```

### CoffeeScript Styleguide

* Set parameter defaults without spaces around the equal sign
    * `clear = (count=1) ->` instead of `clear = (count = 1) ->`
* Use spaces around operators
    * `count + 1` instead of `count+1`
* Use spaces after commas (unless separated by newlines)
* Use parentheses if it improves code clarity.
* Prefer alphabetic keywords to symbolic keywords:
    * `a is b` instead of `a == b`
* Avoid spaces inside the curly-braces of hash literals:
    * `{a: 1, b: 2}` instead of `{ a: 1, b: 2 }`
* Include a single line of whitespace between methods.
* Capitalize initialisms and acronyms in names, except for the first word, which
  should be lower-case:
  * `getURI` instead of `getUri`
  * `uriToOpen` instead of `URIToOpen`
* Use `slice()` to copy an array
* Add an explicit `return` when your function ends with a `for`/`while` loop and
  you don't want it to return a collected array.
* Use `this` instead of a standalone `@`
  * `return this` instead of `return @`

### Specs Styleguide

- Include thoughtfully-worded, well-structured [Jasmine](https://jasmine.github.io/) specs in the `./spec` folder.
- Treat `describe` as a noun or situation.
- Treat `it` as a statement about state or how an operation changes state.

#### Example

```coffee
describe 'a dog', ->
 it 'barks', ->
 # spec here
 describe 'when the dog is happy', ->
  it 'wags its tail', ->
  # spec here
```

### Documentation Styleguide

* Use [AtomDoc](https://github.com/atom/atomdoc).
* Use [Markdown](https://daringfireball.net/projects/markdown).
* Reference methods and classes in markdown with the custom `{}` notation:
    * Reference classes with `{ClassName}`
    * Reference instance methods with `{ClassName::methodName}`
    * Reference class methods with `{ClassName.methodName}`

#### Example

```coffee
# Public: Disable the package with the given name.
#
# * `name`    The {String} name of the package to disable.
# * `options` (optional) The {Object} with disable options (default: {}):
#   * `trackTime`     A {Boolean}, `true` to track the amount of time taken.
#   * `ignoreErrors`  A {Boolean}, `true` to catch and ignore errors thrown.
# * `callback` The {Function} to call after the package has been disabled.
#
# Returns `undefined`.
disablePackage: (name, options, callback) ->
```

## Additional Notes

### Issue and Pull Request Labels

This section lists the labels we use to help us track and manage issues and pull requests. Most labels are used across all Atom repositories, but some are specific to `atom/atom`.

[GitHub search](https://help.github.com/articles/searching-issues/) makes it easy to use labels for finding groups of issues or pull requests you're interested in. For example, you might be interested in [open issues across `atom/atom` and all Atom-owned packages which are labeled as bugs, but still need to be reliably reproduced](https://github.com/search?utf8=%E2%9C%93&q=is%3Aopen+is%3Aissue+user%3Aatom+label%3Abug+label%3Aneeds-reproduction) or perhaps [open pull requests in `atom/atom` which haven't been reviewed yet](https://github.com/search?utf8=%E2%9C%93&q=is%3Aopen+is%3Apr+repo%3Aatom%2Fatom+comments%3A0). To help you find issues and pull requests, each label is listed with search links for finding open items with that label in `atom/atom` only and also across all Atom repositories. We  encourage you to read about [other search filters](https://help.github.com/articles/searching-issues/) which will help you write more focused queries.

The labels are loosely grouped by their purpose, but it's not required that every issue have a label from every group or that an issue can't have more than one label from the same group.

Please open an issue on `atom/atom` if you have suggestions for new labels, and if you notice some labels are missing on some repositories, then please open an issue on that repository.

#### Type of Issue and Issue State

| Label name | `atom/atom` :mag_right: | `atom`‑org :mag_right: | Description |
| --- | --- | --- | --- |
| `enhancement` | [search][search-atom-repo-label-enhancement] | [search][search-atom-org-label-enhancement] | Feature requests. |
| `bug` | [search][search-atom-repo-label-bug] | [search][search-atom-org-label-bug] | Confirmed bugs or reports that are very likely to be bugs. |
| `question` | [search][search-atom-repo-label-question] | [search][search-atom-org-label-question] | Questions more than bug reports or feature requests (e.g. how do I do X). |
| `feedback` | [search][search-atom-repo-label-feedback] | [search][search-atom-org-label-feedback] | General feedback more than bug reports or feature requests. |
| `help-wanted` | [search][search-atom-repo-label-help-wanted] | [search][search-atom-org-label-help-wanted] | The Atom core team would appreciate help from the community in resolving these issues. |
| `beginner` | [search][search-atom-repo-label-beginner] | [search][search-atom-org-label-beginner] | Less complex issues which would be good first issues to work on for users who want to contribute to Atom. |
| `more-information-needed` | [search][search-atom-repo-label-more-information-needed] | [search][search-atom-org-label-more-information-needed] | More information needs to be collected about these problems or feature requests (e.g. steps to reproduce). |
| `needs-reproduction` | [search][search-atom-repo-label-needs-reproduction] | [search][search-atom-org-label-needs-reproduction] | Likely bugs, but haven't been reliably reproduced. |
| `blocked` | [search][search-atom-repo-label-blocked] | [search][search-atom-org-label-blocked] | Issues blocked on other issues. |
| `duplicate` | [search][search-atom-repo-label-duplicate] | [search][search-atom-org-label-duplicate] | Issues which are duplicates of other issues, i.e. they have been reported before. |
| `wontfix` | [search][search-atom-repo-label-wontfix] | [search][search-atom-org-label-wontfix] | The Atom core team has decided not to fix these issues for now, either because they're working as intended or for some other reason. |
| `invalid` | [search][search-atom-repo-label-invalid] | [search][search-atom-org-label-invalid] | Issues which aren't valid (e.g. user errors). |
| `package-idea` | [search][search-atom-repo-label-package-idea] | [search][search-atom-org-label-package-idea] | Feature request which might be good candidates for new packages, instead of extending Atom or core Atom packages. |
| `wrong-repo` | [search][search-atom-repo-label-wrong-repo] | [search][search-atom-org-label-wrong-repo] | Issues reported on the wrong repository (e.g. a bug related to the [Settings View package](https://github.com/atom/settings-view) was reported on [Atom core](https://github.com/atom/atom)). |

#### Topic Categories

| Label name | `atom/atom` :mag_right: | `atom`‑org :mag_right: | Description |
| --- | --- | --- | --- |
| `windows` | [search][search-atom-repo-label-windows] | [search][search-atom-org-label-windows] | Related to Atom running on Windows. |
| `linux` | [search][search-atom-repo-label-linux] | [search][search-atom-org-label-linux] | Related to Atom running on Linux. |
| `mac` | [search][search-atom-repo-label-mac] | [search][search-atom-org-label-mac] | Related to Atom running on macOS. |
| `documentation` | [search][search-atom-repo-label-documentation] | [search][search-atom-org-label-documentation] | Related to any type of documentation (e.g. [API documentation](https://atom.io/docs/api/latest/) and the [flight manual](https://flight-manual.atom.io/)). |
| `performance` | [search][search-atom-repo-label-performance] | [search][search-atom-org-label-performance] | Related to performance. |
| `security` | [search][search-atom-repo-label-security] | [search][search-atom-org-label-security] | Related to security. |
| `ui` | [search][search-atom-repo-label-ui] | [search][search-atom-org-label-ui] | Related to visual design. |
| `api` | [search][search-atom-repo-label-api] | [search][search-atom-org-label-api] | Related to Atom's public APIs. |
| `uncaught-exception` | [search][search-atom-repo-label-uncaught-exception] | [search][search-atom-org-label-uncaught-exception] | Issues about uncaught exceptions, normally created from the [Notifications package](https://github.com/atom/notifications). |
| `crash` | [search][search-atom-repo-label-crash] | [search][search-atom-org-label-crash] | Reports of Atom completely crashing. |
| `auto-indent` | [search][search-atom-repo-label-auto-indent] | [search][search-atom-org-label-auto-indent] | Related to auto-indenting text. |
| `encoding` | [search][search-atom-repo-label-encoding] | [search][search-atom-org-label-encoding] | Related to character encoding. |
| `network` | [search][search-atom-repo-label-network] | [search][search-atom-org-label-network] | Related to network problems or working with remote files (e.g. on network drives). |
| `git` | [search][search-atom-repo-label-git] | [search][search-atom-org-label-git] | Related to Git functionality (e.g. problems with gitignore files or with showing the correct file status). |

#### `atom/atom` Topic Categories

| Label name | `atom/atom` :mag_right: | `atom`‑org :mag_right: | Description |
| --- | --- | --- | --- |
| `editor-rendering` | [search][search-atom-repo-label-editor-rendering] | [search][search-atom-org-label-editor-rendering] | Related to language-independent aspects of rendering text (e.g. scrolling, soft wrap, and font rendering). |
| `build-error` | [search][search-atom-repo-label-build-error] | [search][search-atom-org-label-build-error] | Related to problems with building Atom from source. |
| `error-from-pathwatcher` | [search][search-atom-repo-label-error-from-pathwatcher] | [search][search-atom-org-label-error-from-pathwatcher] | Related to errors thrown by the [pathwatcher library](https://github.com/atom/node-pathwatcher). |
| `error-from-save` | [search][search-atom-repo-label-error-from-save] | [search][search-atom-org-label-error-from-save] | Related to errors thrown when saving files. |
| `error-from-open` | [search][search-atom-repo-label-error-from-open] | [search][search-atom-org-label-error-from-open] | Related to errors thrown when opening files. |
| `installer` | [search][search-atom-repo-label-installer] | [search][search-atom-org-label-installer] | Related to the Atom installers for different OSes. |
| `auto-updater` | [search][search-atom-repo-label-auto-updater] | [search][search-atom-org-label-auto-updater] | Related to the auto-updater for different OSes. |
| `deprecation-help` | [search][search-atom-repo-label-deprecation-help] | [search][search-atom-org-label-deprecation-help] | Issues for helping package authors remove usage of deprecated APIs in packages. |
| `electron` | [search][search-atom-repo-label-electron] | [search][search-atom-org-label-electron] | Issues that require changes to [Electron](https://electron.atom.io) to fix or implement. |

#### Pull Request Labels

| Label name | `atom/atom` :mag_right: | `atom`‑org :mag_right: | Description
| --- | --- | --- | --- |
| `work-in-progress` | [search][search-atom-repo-label-work-in-progress] | [search][search-atom-org-label-work-in-progress] | Pull requests which are still being worked on, more changes will follow. |
| `needs-review` | [search][search-atom-repo-label-needs-review] | [search][search-atom-org-label-needs-review] | Pull requests which need code review, and approval from maintainers or Atom core team. |
| `under-review` | [search][search-atom-repo-label-under-review] | [search][search-atom-org-label-under-review] | Pull requests being reviewed by maintainers or Atom core team. |
| `requires-changes` | [search][search-atom-repo-label-requires-changes] | [search][search-atom-org-label-requires-changes] | Pull requests which need to be updated based on review comments and then reviewed again. |
| `needs-testing` | [search][search-atom-repo-label-needs-testing] | [search][search-atom-org-label-needs-testing] | Pull requests which need manual testing. |


[dev_email]: mailto:dev@cloudyrock.io



