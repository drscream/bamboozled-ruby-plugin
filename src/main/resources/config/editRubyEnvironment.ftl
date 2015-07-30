[#-- @ftlvariable name="uiConfigBean" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ui.bambooSection titleKey="misc.ruby.title"]

	[@ww.select cssClass="builderSelectWidget" labelKey='ruby.config.runtime' name='custom.ruby-config-runtime'
	            list=uiConfigBean.getExecutableLabels('ruby')
	            extraUtility=addExecutableLink 
	            required='true' /]
	
	[@ww.textfield labelKey='ruby.config.environment' name='custom.ruby-config-environmentVariables' required='false' cssClass="long-field" /]
	
[/@ui.bambooSection]	                  