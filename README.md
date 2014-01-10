# Bamboozled Ruby Plugin

This continuous integration/continuous deployment plugin for Atlassian [Bamboo](http://www.atlassian.com/software/bamboo/overview) enables easy configuration of build and deployment tasks to run various ruby based build tools including bundler, rake, and capistrano.

## Features for both build and deploy plans
Via [Bamboo](http://www.atlassian.com/software/bamboo/overview) tasks, this plugin enables configure a ruby runtime environment once per plan and run:
* Bundler
* Rake
* Capistrano

## Shared Configuration
Using the miscellaneous tab on the plan configuration, you can select the appropriate RVM environment as well as specify common environment variables such as `RAILS_ENV`.  These settings are shared across all build and deploy tasks.

## RVM Support
The preferred method for managing ruby runtimes and gemsets is [RVM](http://rvm.io).  

Notes: 

1.  The RVM utilized is exclusively designated in the Bamboo Plan Miscellaneous tab and does not recognize any files such as .rvmrc, .ruby-version or .ruby-manager.  
2.  If your latest RVM gemset is not showing up in the dropdown,  navigate to the Bamboo `Administration | Server capabilities` and click `Detect server capabilities`

## Download
[Download the plugin binary](https://www.dropbox.com/sh/of18yggwwfs7i69/sGDkijyaPW) 

## Installation and Usage
This is still a bit clunky, but a shared runtime configuration is much more maintainable than the known alternatives.  (ideas are welcome)

1. Install the `bamboozled-ruby-plugin`
  * Obtain via the Atlassian Plugin site (TODO)
  * or [download the binary](https://www.dropbox.com/sh/of18yggwwfs7i69/sGDkijyaPW) and upload to via the Bamboo universal plugin manager. 
2. Navigate to the Bamboo `Administration | Server capabilities` and click `Detect server capabilities`, this will detect existing RVM/ruby and their associated gemsets.
3. Configure a *New* Plan:
  * `Create | Create a new plan` and fill out appropriate information then click `Configure tasks`
  * **Skip** adding tasks and click `Create` *without* the plan enabled *(this is a workaround for configuring a global ruby runtime)*
  * Now follow the existing plan instructions starting with second bullet below  
4. Existing Plan
  * Choose `Actions | Configure plan`
  * Choose the `Miscellaneous` tab to select the appropriate RVM or ruby runtime and set any common environment variables
  * Choose the `Tasks` tab
  * Choose the `Stage` i.e. `Default Stage`
  * Click `Add task` and choose your weapon of choice i.e. `Bundler Install`, `Bundler CLI`, `Rake`, or `Capistrano`

## Bundler Features

#### Bundler Install
The `Bundler Install` task by default is setup to quickly execute `bundle install` dependencies during the build process. Note will only work when RVM is installed in the home directory of the user the build server is running under.

#### Bundler CLI
The `Bundler CLI` is setup to allow for any variations on command line execution of `bundler`

#### Bundle exec with Rake
The `Rake` task has the option to be run from `bundle exec`


## Test Reporting

### RSpec
To enable the [RSpec JUnit XML Formatter](https://github.com/sj26/rspec_junit_formatter)

1. Add the this fragment to your Gemfile.

        group :test do
            gem "rspec_junit_formatter"
        end

2. Edit your the .rspec file in the base of your project and replace the contents with.

        --format RspecJunitFormatter
        --out test-reports/rspec.xml

3. Add a JUnit Parser task to the `Final tasks` section of your Job with `**/test-reports/*.xml` in the `Specify custom results directories` field. 

### Cucumber
1. Edit the `config/cucumber.yml` and change the `std_opts` to include the `junit` formatter as well as specifiy the output directory. For example:

		std_opts = "-r features/support/ -r features/step_definitions --quiet -f pretty -f junit -o test-reports --strict --tags ~@wip --tags ~@todo"

2. Add a JUnit Parser task to the `Final tasks` section of your Job with `**/test-reports/*.xml` in the `Specify custom results directories` field (if not already done for rspec above).

## Example Configurations

### Example Build Configuration
*Rails app to be deployed to AWS Elastic Beanstalk*
Assumes you have followed _Installation and Usage_ above.

1.  Add a _Source Code Checkout_ task
2.  Add a _Bundler Install_ task (it should display the RVM chosen if you followed the _Installation and Usage_ instructions)
3.  Add a _Rake_ task:

        Tasks: db:drop db:create db:migrate db:seed spec cucumber
        Additional Environment Variables: RAILS_ENV=test
        Bundler Exec: checked
4. _Optional_ step to package [elastic-beanstalk](https://github.com/alienfast/elastic-beanstalk) for deployment.  Add a _Rake_ task:

        Tasks: eb:package
        Additional Environment Variables: RAILS_ENV=test
        Bundler Exec: checked

### Example Deploy Configuration
*To AWS Elastic Beanstalk*
Assumes you have followed the _Example Build Configuration_ above, and that the build produces the artifacts `eb:package`, `eb:yml`, `eb:gemfiles` for an [elastic-beanstalk](https://github.com/alienfast/elastic-beanstalk) deployment.

1.  Add a _Clean working directory task_.
2.  Add an _Artifact download task_ with each of `eb:package`, `eb:yml`, `eb:gemfiles` listed.
3.  Add a _Bundler Install_ task (it should display the RVM chosen if you followed the _Installation and Usage_ instructions)
4.  Add a _Bundler CLI_ task:

        Task description: bundle binstubs elastic-beanstalk
        Arguments: binstubs elastic-beanstalk
5.  Add a _Bundle CLI_ task:

        Task description: bundle exec ./bin/elastic-beanstalk eb:deploy
        Arguments: ./bin/elastic-beanstalk eb:deploy[${bamboo.buildNumber}]
        Additional Environment Variables: RAILS_ENV=staging
        Bundler Exec: checked

## Other stuff

### Fork
This code was originally forked from the [rake-bamboo-plugin version 2.1](https://github.com/wolfeidau/rake-bamboo-plugin) published by Mark Wolfe.  We wanted to move the plugin forward in a specific way, so we brought it into our own repository.  Many thanks to Mark Wolfe for establishing a solid codebase.

### Supported and Unsupported environment/OS/etc
We use Linux, RVM, Bundler and Rake in a continuous integration/continuous deployment environment.  These will have the most attention given to them, while Windows and other ruby configurations (i.e. rbenv, system rubies) will likely be neglected unless contributors step up to fill the gaps (which we welcome!).

### Speed/Agility/Backwards compatibility
While we do care about backwards compatibility, we care most about being easy to use and having a high degree of utility.  Additions and changes will come readily if there is a need, so be aware when upgrading.  If your primary concern is backwards compatibility, please take a look at the [rake-bamboo-plugin](https://github.com/wolfeidau/rake-bamboo-plugin) instead as strong backwards compatibility is a goal. 

## Contributing

Please contribute! We will readily accept contributions and try to stay on top of them.  Any contribution should contain additional Junit tests and all tests should pass.

### To Do:
- [ ] More reuse to reduce duplicate code
- [ ] Make available via Atlassian UPM (@rosskevin)

### To contribute:

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

### Formatting:
We include an eclipse `formatting-standard.xml` in the root to make changes easier to understand for all contributors.
