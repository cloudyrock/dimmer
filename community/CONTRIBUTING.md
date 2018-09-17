# Contributing to Dimmer project

:+1::tada: First off, thanks for taking the time to contribute! :tada::+1:

The following is a set of guidelines for contributing to Dimmer project which is hosted on GitHub. These are mostly guidelines, not rules. Use your best judgment, and feel 
free to propose changes to this document in a pull request.


## Table of contents

   * [Code of Conduct](#code-of-conduct)
   * [It's just a question, I don't want to read this whole thing!!!](#its-just-a-question-i-dont-want-to-read-this-whole-thing)
   * [Dimmer project packages](#dimmer-project-packages)
   * [How Can I Contribute?](#how-can-i-contribute)
      * [Reporting Bugs](#reporting-bugs)
         * [Before Submitting A Bug Report](#before-submitting-a-bug-report)
         * [How Do I Submit A (Good) Bug Report?](#how-do-i-submit-a-good-bug-report)
      * [Suggesting Enhancements](#suggesting-enhancements)
         * [Before Submitting An Enhancement Suggestion](#before-submitting-an-enhancement-suggestion)
         * [How Do I Submit A (Good) Enhancement Suggestion?](#how-do-i-submit-a-good-enhancement-suggestion)
      * [Your First Code Contribution](#your-first-code-contribution)
         * [Before creating a pull request](#before-creating-a-pull-request)
            * [Code style](#code-style)
            * [Tests](#tests)
            * [JavaDocs and general documentation](#javadocs-and-general-documentation)
         * [How Do I perform A (Good) Pull Request?](#how-do-i-perform-a-good-pull-request)
      * [Issue and Pull Request Labels](#issue-and-pull-request-labels)
         * [Type of issue](#type-of-issue)
         * [Module](#module)
         * [Severity](#severity)
         * [Complexity](#complexity)
         * [Others](#others)

## Code of Conduct

This project and everyone participating in it is governed by the [Dimmer Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. 
Please report unacceptable behavior to [dev@cloudyrock.io][dev_email].

## It's just a question, I don't want to read this whole thing!!!

> **Note:** Please don't file an issue to ask a question.
 
We have an official [FAQ page](FAQ.md) where your question may be already answered. Otherwise an email to [dev@cloudyrock][dev_email]  will be the fastest way to get 
tour question resolved. 
 
## Dimmer project packages
Dimmer project contains 4 main areas. 
- **Core:** Base project for the basic Dimmer module and Dimmer server. Issue label is `core-module`
- **Dimmer:** Basic Dimmer module to used locally or as a client in combination with Dimmer server. Issue label is `local-module`
- **Dimmer server:** Dimmer as a centralised server. Issue label is `server-module`
- **Samples:** Set of examples using Dimmer project.  Issue label is `samples-module`
 
## How Can I Contribute?

### Reporting Bugs

This section guides you through submitting a bug report for Dimmer project. 

> **Note:** If you find a **Closed** issue that seems like it is the same thing that you're experiencing, open a new issue and include a link to the original issue in the body of your new one.

#### Before Submitting A Bug Report

* **Check the [FAQ page](FAQ.md)** for a list of common questions and problems. You might find out that issue/question is clarified in this page.
* **Perform a [cursory search](https://github.com/issues?utf8=%E2%9C%93&q=is%3Aissue+archived%3Afalse+repo%3Acloudyrock%2Fdimmer-project+)** 
to see if the problem has already been reported. If it has **and the issue is still open**, add a comment to the existing issue instead of opening a new one.

#### How Do I Submit A (Good) Bug Report?

Bugs are tracked as [GitHub issues](https://guides.github.com/features/issues/). Create an issue on the Dimmer repository following the
[the issue template](ISSUE_TEMPLATE.md). Please provide as much information as you can and add the following labels to make easier to categorise issues: 
- The label `bug`
- The module or modules involved: [module labels](#module)
- Severity of the bug: [severity labels](#severity)
- If you feel confident to estimate how complex the fix is, please provide a complexity label: [complexity labels](#complexity)

### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion for Dimmer project, including completely new features and minor improvements to existing functionality.

#### Before Submitting An Enhancement Suggestion


* **Check the [FAQ page](FAQ.md)**. You might find out that enhancement is already covered.
* **Determine [which module the enhancement should be reported in](#dimmer-project-packages)**.
* **Perform a [cursory search](https://github.com/issues?utf8=%E2%9C%93&q=is%3Aissue+archived%3Afalse+repo%3Acloudyrock%2Fdimmer-project+)** to see if the problem has already been reported. If it has **and the issue is still open**, add a comment to the existing issue instead of opening a new one.


#### How Do I Submit A (Good) Enhancement Suggestion?

Enhancement suggestions are tracked as [GitHub issues](https://guides.github.com/features/issues/). Create an enhancement on the main repository and provide the required information  
by filling in [the enhancement template](ENHANCENMENT_TEMPLATE.md).
 
Please add the following labels to make easier to categorise issues:
- The label `feature`
- The module or modules involved: [module labels](#module)
- Severity of the bug: [severity labels](#severity)
- If you feel confident to estimate how complex the fix is, please provide a complexity label: [complexity labels](#complexity

### Your First Code Contribution

Unsure where to begin contributing to Dimmer project? You can start by looking through these `beginner` and `intermediate` issues:

* [Beginner issues](https://github.com/cloudyrock/dimmer-project/issues?q=is%3Aopen+is%3Aissue+label%3Abeginner) - issues which should only require a few lines of code, and a test or two.
* [Intermediate issues](https://github.com/cloudyrock/dimmer-project/issues?q=is%3Aopen+is%3Aissue+label%3Aadvance) - issues which should be a bit more involved than `beginner` issues.


#### Before creating a pull request

##### Code style

While we improve this section, please adhere to the [Google Style guide](http://google.github.io/styleguide/javaguide.html)

##### Tests

- Any code delivered should be covered by unit and integration tests.

- Unit test files should end with the sufix `UTest` and integration tests with `ITest`.

- We use the shouldXXX_whenXXX_ifXXXXX for test names. Something like shouldReturn4_whenSum_ifParametersAre2And2()

- Ensure the test coverage fits the threshold 

##### JavaDocs and general documentation

We believe in self explanatory code, so please prioritize the readable code over documentation. However, as we are developing 
a library, please ensure you provide(and update) the javaDocs for the public API. 

#### How Do I perform A (Good) Pull Request?
- Fork repository to
- Checkout a new branch called feature/issue-XXX
- Perform the change
    - Actual code change
    - Tests
    - Java docs for public API
    - Update readme if needed
- Perform pull request, filling in [the required template](PULL_REQUEST_TEMPLATE.md)
- Check travis and sonar are passing


### Issue and Pull Request Labels

This section lists the labels we use to help us track and manage issues and pull requests. 

The labels are loosely grouped by their purpose, but it's not required that every issue have a label from every group or that an issue can't have more than one label from the same group. However,
the more you provide, the easier and faster the issue can be fixed.

Please click on the `search` list the issues for the given label.


#### Type of issue

| Label name | `Search in github` :mag_right: | Description |
| --- | --- | --- |
| `bug` | [search][search-repo-label-bug] | Bug issues |
| `feature` | [search][search-repo-label-feature] | Feature issues |
| `docs/ops` | [search][search-repo-label-docs-ops] | Issues which just require documentation or some management work |


#### Module

| Label name | `Search in github` :mag_right: | Description |
| --- | --- | --- |
| `core-module` | [search][search-repo-label-core-module] | issues to be fixed in the core |
| `local-module` | [search][search-repo-label-local-module] | issues to be fixed in the local module |
| `server-module` | [search][search-repo-label-server-module] | issues to be fixed in the server module |
| `samples-module` | [search][search-repo-label-samples-module] | issues to be fixed in one of the sample apps|

#### Severity

| Label name | `Search in github` :mag_right: | Description |
| --- | --- | --- |
| `critical` | [search][search-repo-label-critical] | Critical issues stopping Dimmer from being used |
| `normal` | [search][search-repo-label-normal] | normal issues that needs to be fixed, but doesn't stop the application from being used  |
| `minor` | [search][search-repo-label-minor] | issues that can wait or it's a nice to have feature |

#### Complexity

| Label name | `Search in github` :mag_right: | Description |
| --- | --- | --- |
| `beginner` | [search][search-repo-label-beginner] | issues which should only require a few lines of code, and a test or two |
| `intermediate` | [search][search-repo-label-intermediate] | issues which should be a bit more involved than `beginner` issues  |
| `advance` | [search][search-repo-label-advance] | issues that require some deep knowledge, expertise or just time consuming |

#### Others

| Label name | `Search in github` :mag_right: | Description |
| --- | --- | --- |
| `invalid/won't fix` | [search][search-repo-label-wont-fix] | issues which won't be worked on|



[dev_email]: mailto:dev@cloudyrock.io


[search-repo-label-bug]:https://github.com/cloudyrock/dimmer-project/labels/bug
[search-repo-label-feature]:https://github.com/cloudyrock/dimmer-project/labels/feature
[search-repo-label-docs-ops]:https://github.com/cloudyrock/dimmer-project/labels/docs%2Fops

[search-repo-label-core-module]:https://github.com/cloudyrock/dimmer-project/labels/core-module
[search-repo-label-local-module]:https://github.com/cloudyrock/dimmer-project/labels/local-module
[search-repo-label-server-module]:https://github.com/cloudyrock/dimmer-project/labels/server-module
[search-repo-label-samples-module]:https://github.com/cloudyrock/dimmer-project/labels/samples-module

[search-repo-label-critical]:https://github.com/cloudyrock/dimmer-project/labels/critical
[search-repo-label-normal]:https://github.com/cloudyrock/dimmer-project/labels/normal
[search-repo-label-minor]:https://github.com/cloudyrock/dimmer-project/labels/minor

[search-repo-label-beginner]:https://github.com/cloudyrock/dimmer-project/labels/beginner
[search-repo-label-intermediate]:https://github.com/cloudyrock/dimmer-project/labels/intermediate
[search-repo-label-advance]:https://github.com/cloudyrock/dimmer-project/labels/advance

[search-repo-label-wont-fix]:https://github.com/cloudyrock/dimmer-project/labels/invalid%2Fwon%27t%20fix