package ncp.repository;

import ncp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(
            value = "SELECT count(*) FROM Address a WHERE a.flat = ?1 AND a.building = ?2 AND a.house = ?3 "
            + "AND a.street = ?4 AND a.city = ?5 AND a.region = ?6 AND a.country = ?7"
            , nativeQuery = true
    )
    Long countByFlatAndBuildingAndHouseAndStreetAndCityAndRegionAndCountry(Long flat, Long building, Long house
                                                , String street, String city, String region, String country);

    @Query(
            value = "SELECT * FROM address ORDER BY country, region, city, street, house, building, flat"
            + " LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Address> selectByLimitOffset(Long limit, Long offset);

    @Query(
            value = "SELECT COUNT(*) FROM address a WHERE a.country =?1 AND a.region =?2 AND a.city=?3 AND a.street=?4" +
                    " AND a.house=?5 AND a.building=?6 AND a.flat IS NOT NULL"
            , nativeQuery = true
    )
    Long countCountyRegionCityStreetHouseBuildingsWithFlats(String country, String region, String city, String street, long house, long building);

    @Query(
            value = "SELECT COUNT(*) FROM address a WHERE a.country =?1 AND a.region =?2 AND a.city=?3 AND a.street=?4" +
                    " AND a.house=?5 AND a.building IS NOT NULL"
            , nativeQuery = true
    )
    Long countCountyRegionCityStreetHouseWithBuildings(String country, String region, String city, String street, long house);

    @Query(
            value = "SELECT COUNT(*) FROM address a WHERE a.country =?1 AND a.region =?2 AND a.city=?3 AND a.street IS NOT NULL"
            , nativeQuery = true
    )
    Long countCountyRegionCityWithStreets(String country, String region, String city);

    @Query(
            value = "SELECT COUNT(*) FROM address a WHERE a.country =? AND a.region IS NOT NULL"
            , nativeQuery = true
    )
    Long countCountyWithRegions(String country);

    @Query(
            value = "SELECT * FROM address WHERE country LIKE ?1 AND region LIKE ?2 AND city LIKE ?3 AND street LIKE ?4" +
                    " AND house LIKE ?5 AND building LIKE ?6 AND flat LIKE ?7" +
                    " LIMIT ?8 OFFSET ?9"
            , nativeQuery = true
    )
    List<Address> searchAddress(String country, String region, String city, String street
            , String house, String building, String flat, Long limit, Long offset);


    @Query(
            value = "SELECT a.* FROM address a" +
                    " LEFT JOIN transmitter_available_addresses taa" +
                    " on a.id = taa.available_addresses_id AND ?10 = taa.transmitter_id" +
                    " WHERE a.country LIKE ?1 AND a.region LIKE ?2 AND a.city LIKE ?3 AND a.street LIKE ?4" +
                    " AND a.house LIKE ?5 AND a.building LIKE ?6 AND a.flat LIKE ?7" +
                    " AND taa.available_addresses_id IS NULL AND taa.transmitter_id IS NULL" +
                    " LIMIT ?8 OFFSET ?9"
            , nativeQuery = true
    )
    List<Address> searchAddressUnconnectedToTransmitterId(String country, String region, String city, String street
            , String house, String building, String flat, Long limit, Long offset, Long transmitterId);


    @Query(
            value = "SELECT a.*" +
                    " FROM transmitter_available_addresses taa" +
                    " INNER JOIN transmitter t ON taa.transmitter_id = t.id" +
                    " INNER JOIN address a ON taa.available_addresses_id = a.id" +
                    " WHERE t.id=?1" +
                    " LIMIT ?2 OFFSET ?3"
            , nativeQuery = true
    )
    List<Address> selectAvailableAddressByLimitOffsetAndId(Long transmitterId, Long limit, Long offset);
}
