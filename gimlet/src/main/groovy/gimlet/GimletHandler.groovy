package gimlet

class GimletHandler {
	GimletConfig config
	Closure closure
	Map splat

	def execute(Binding binding) {
		def result = null
		binding.setProperty('splat', splat)

		if (config.enrich) {
			def cloned = config.enrich.clone()
			cloned.resolveStrategy = Closure.DELEGATE_FIRST
			cloned.call(binding)
		}

		use ([ * config.categories]) {
			if (config.before) {
				delegateAndExecute(config.before.clone(), binding)	
			}

			result = delegateAndExecute(closure, binding)
				
	        if (config.after) {
	        	delegateAndExecute(config.after.clone(), binding)
	        }
		}

		result
	}

	def delegateAndExecute(Closure action, Binding binding) {
        action.resolveStrategy = Closure.DELEGATE_FIRST
        action.delegate = binding
        action.call()
	}

}