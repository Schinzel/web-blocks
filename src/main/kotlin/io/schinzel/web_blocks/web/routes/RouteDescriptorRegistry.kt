package io.schinzel.web_blocks.web.routes

import kotlin.reflect.KClass

/**
 * The purpose of this class is to store route generators.
 *
 * That is a class that can generate a route for a given class.
 */
object RouteDescriptorRegistry {
    private val generators =
        mutableMapOf<KClass<out IRoute>, IRouteDescriptor<out IRoute>>()

    fun <T : IRoute> register(
        processorType: KClass<T>,
        generator: IRouteDescriptor<T>,
    ) {
        generators[processorType] = generator
    }

    @Suppress("UNCHECKED_CAST")
    fun getRouteDescriptor(clazz: KClass<out IRoute>): IRouteDescriptor<IRoute> =
        generators.entries
            .find { (interfaceType, _) ->
                interfaceType.java.isAssignableFrom(clazz.java)
            }?.value as? IRouteDescriptor<IRoute>
            ?: throw IllegalArgumentException("No route generator registered for ${clazz.simpleName}")
}
