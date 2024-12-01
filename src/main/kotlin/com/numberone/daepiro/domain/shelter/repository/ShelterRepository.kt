package com.numberone.daepiro.domain.shelter.repository

import com.numberone.daepiro.domain.shelter.entity.Shelter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ShelterRepository : JpaRepository<Shelter, Long> {
    @Query(
        value = "SELECT id, name, latitude, longitude, address, type " +
            "FROM shelter " +
            "WHERE type = :type " +
            "ORDER BY (6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(latitude)) * " +
            "COS(RADIANS(longitude) - RADIANS(:longitude)) + " +
            "SIN(RADIANS(:latitude)) * SIN(RADIANS(latitude)))) " +
            "LIMIT 10",
        nativeQuery = true
    )
    fun findTop10ClosestShelters(
        @Param("longitude") longitude: Double,
        @Param("latitude") latitude: Double,
        @Param("type") type: String
    ): List<Shelter>
}
