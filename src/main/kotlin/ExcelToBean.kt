package com.poiji

import java.beans.Introspector
import java.lang.reflect.Field
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaMethod
import java.util.NoSuchElementException



class ExcelToBean(mappingConfig: ExcelToBean.() -> Unit) {
    private val mappingsKotlin: MutableMap<KProperty<*>, Int> = mutableMapOf()
    private val mappingsJava: MutableMap<Field, Int> = mutableMapOf()
    private val titleMappingsKotlin: MutableMap<KProperty<*>, String> = mutableMapOf()
    private val titleMappingsJava: MutableMap<Field, String> = mutableMapOf()
    init {
        mappingConfig()
    }

    fun excelRow(property: KProperty<*>) {

    }

    fun excelRow(function: KFunction<*>) {

    }

    infix fun <R> KProperty<R>.at(i: Int) {
        mappingsKotlin[this] = i
    }

    infix fun <R> KFunction<R>.at(i: Int) {
        mappingsJava[getField(this)] = i
    }

    infix fun <R> KProperty<R>.at(t: String) {
        titleMappingsKotlin[this] = t
    }

    infix fun <R> KFunction<R>.at(t: String) {
        titleMappingsJava[getField(this)] = t
    }

    fun get(property: KProperty<*>) = mappingsKotlin[property] ?: titleMappingsKotlin[property]
    fun get(field: Field) = mappingsJava[field] ?: titleMappingsJava[field]
    fun get(function: KFunction<*>) = mappingsJava[getField(function)] ?: titleMappingsJava[getField(function)]

    private fun getField(function: KFunction<*>): Field {
        val klazz = function.javaMethod?.declaringClass
        val bean = Introspector.getBeanInfo(klazz)
        return try {
            bean.propertyDescriptors
                .filter { it.readMethod == function.javaMethod || it.writeMethod == function.javaMethod }
                .map { it.name }
                .map { klazz?.getDeclaredField(it) }
                .first()!!
        } catch (e: Exception) {
            throw IllegalArgumentException("Method ${function.javaMethod} Unknown", e)
        }

    }
}


