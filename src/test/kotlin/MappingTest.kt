package com.poiji

import com.poiji.deserialize.model.byname.PersonByName
import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.*
import kotlin.test.assertFailsWith

class MappingTest {
    @Test
    fun basicMappingTest() {
        val mappings = ExcelToBean() {
            excelRow(Person::row)
            Person::name at 0
            Person::address at 1
            Person::mobile at 2
            Person::email at 3

            excelRow(PersonByName::getRow)
            PersonByName::getName at "Name"
        }

        assertEquals(0, mappings.get(Person::name))
        assertEquals(1, mappings.get(Person::address))
        assertEquals(2, mappings.get(Person::mobile))
        assertEquals(3, mappings.get(Person::email))
        assertNull(mappings.get(Vehicle::name))
        assertFailsWith<IllegalArgumentException> { mappings.get(Vehicle::hashCode) }

        assertEquals("Name", mappings.get(PersonByName::getName))
    }
}




