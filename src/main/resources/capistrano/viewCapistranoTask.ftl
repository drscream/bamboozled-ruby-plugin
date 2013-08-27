<!-- Display the runtime and a tickler to update it if necessary -->
[#if invalidRuntime]	
	[@ww.label labelKey='ruby.config.runtime.invalid' 
						required='true' 
						cssClass='error'
						value='Set in the Miscellaneous tab. Execution will fail until runtime is chosen.'/]
[#else]
	[@ww.label labelKey='ruby.config.runtime' name='custom.ruby-config-runtime'/]
[/#if]

[@ww.label labelKey="capistrano.tasks" name="tasks"/]

<!-- Additional config environment variables -->
[@ww.label labelKey='ruby.config.environment' name='custom.ruby-config-environmentVariables'/]
[@ww.label labelKey='common.environment' name='environmentVariables'/]
[@ww.label labelKey="capistrano.bundleexec" name="bundleexec"/]
[@ww.label labelKey="capistrano.debug" name="debug"/]
[@ww.label labelKey="capistrano.verbose" name="verbose"/]