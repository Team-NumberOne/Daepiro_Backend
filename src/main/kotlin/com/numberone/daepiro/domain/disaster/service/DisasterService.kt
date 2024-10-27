package com.numberone.daepiro.domain.disaster.service

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.vo.AddressInfo
import com.numberone.daepiro.domain.disaster.entity.Disaster
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.repository.DisasterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DisasterService(
    private val disasterRepository: DisasterRepository,
    private val addressRepository: AddressRepository
) {
    fun getDisasterByAddressAndType(
        addresses: List<Address>,
        types: List<DisasterType>
    ): List<Disaster> {
        val addressIds = mutableSetOf<Long>()
        val disasterTypes = types.map { it.type }

        for (address in addresses) {
            val addressInfo = AddressInfo.from(address)
            val parentAddressIds = addressRepository.findParentAddress(addressInfo)
                .map { it.id!! }
            addressIds.addAll(parentAddressIds + address.id!!)
        }
        return disasterRepository.findByAddressIdIn(addressIds)
            .filter { disasterTypes.contains(it.disasterType.type) }
    }

    fun getDisasterByAddressAndType(
        address: Address,
        types: List<DisasterType>
    ): List<Disaster> {
        return getDisasterByAddressAndType(listOf(address), types)
    }
}
