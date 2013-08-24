[#-- @ftlvariable name="uiConfigBean" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

<!-- Display the runtime and a tickler to update it if necessary -->
[#if invalidRuntime]	
	[@ww.label labelKey='ruby.config.runtime.invalid' 
						required='true' 
						cssClass='error'
						value='Set in the Miscellaneous tab. Execution will fail until runtime is chosen.'/]
[#else]
	[@ww.label labelKey='ruby.config.runtime' name='custom.ruby-config-runtime'/]
[/#if]

[@ww.textfield labelKey='ruby.workingSubDirectory' name='workingSubDirectory' required='false' cssClass="long-field" /]
[@ww.textfield labelKey='bundler.path' name='path' required='false' cssClass="long-field" /]

<!-- Additional config environment variables -->
[@ww.label labelKey='ruby.config.environment' name='custom.ruby-config-environmentVariables'/]


[@ww.textfield labelKey='bundler.environment' name='environmentVariables' required='false' cssClass="long-field" /]
[@ww.checkbox labelKey='bundler.binstubs'
                  name='binstubs'
                  toggle='true' /]