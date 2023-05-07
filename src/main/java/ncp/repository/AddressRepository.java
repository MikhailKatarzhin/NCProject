package ncp.repository;

import ncp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(
            value = "SELECT count(*) FROM Address WHERE flat = ?1 AND building = ?2 AND house = ?3 "
                    + "AND street = ?4 AND city = ?5 AND region = ?6 AND country = ?7"
            , nativeQuery = true
    )
    Long countByFlatAndBuildingAndHouseAndStreetAndCityAndRegionAndCountry(Long flat, Long building, Long house
            , String street, String city, String region, String country);

    @Query(
            value = "SELECT count(*) FROM Address WHERE flat IS NULL AND building = ?1 AND house = ?2 "
                    + "AND street = ?3 AND city = ?4 AND region = ?5 AND country = ?6"
            , nativeQuery = true
    )
    Long countByBuildingAndHouseAndStreetAndCityAndRegionAndCountryFlatIsNull(Long building, Long house
            , String street, String city, String region, String country);

    @Query(
            value = "SELECT * FROM address ORDER BY country, region, city, street, house, building, flat"
                    + " LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Address> selectByLimitOffset(Long limit, Long offset);

    @Query(
            value = "SELECT COUNT(*) FROM address WHERE country =?1 AND region =?2 AND city=?3 AND street=?4" +
                    " AND house=?5 AND building=?6 AND flat IS NOT NULL"
            , nativeQuery = true
    )
    Long countCountyRegionCityStreetHouseBuildingsWithFlats(String country, String region, String city, String street, Long house, Long building);

    @Query(
            value = "SELECT COUNT(*) FROM address WHERE country =?1 AND region =?2 AND city=?3 AND street=?4" +
                    " AND house=?5 AND building IS NOT NULL"
            , nativeQuery = true
    )
    Long countCountyRegionCityStreetHouseWithBuildings(String country, String region, String city, String street, Long house);

    @Query(
            value = "SELECT COUNT(*) FROM address WHERE country =?1 AND region =?2 AND city=?3" +
                    " AND street IS NOT NULL AND street <> ''"
            , nativeQuery = true
    )
    Long countCountyRegionCityWithStreets(String country, String region, String city);

    @Query(
            value = "SELECT COUNT(*) FROM address WHERE country = ? AND region IS NOT NULL AND region <> ''"
            , nativeQuery = true
    )
    Long countCountryWithRegions(String country);

    @Query(
            value = "SELECT * FROM address WHERE country LIKE ?1 AND region LIKE ?2 AND city LIKE ?3 AND street LIKE ?4" +
                    " AND CAST (house AS TEXT) LIKE ?5 AND CAST (building AS TEXT) LIKE ?6 AND CAST (flat AS TEXT) LIKE ?7" +
                    " LIMIT ?8 OFFSET ?9"
            , nativeQuery = true
    )
    List<Address> searchAddress(String country, String region, String city, String street
            , String house, String building, String flat, Long limit, Long offset);

    @Query(
            value = "SELECT * FROM address WHERE country LIKE ?1 AND region LIKE ?2 AND city LIKE ?3 AND street LIKE ?4" +
                    " AND CAST (house AS TEXT) LIKE ?5 AND CAST (building AS TEXT) LIKE ?6" +
                    " LIMIT ?7 OFFSET ?8"
            , nativeQuery = true
    )
    List<Address> searchAddressLikeAddressFlatIsNull(String country, String region, String city, String street
            , String house, String building, Long limit, Long offset);

    @Query(
            value = "SELECT a.* FROM address a" +
                    " LEFT JOIN transmitter_available_addresses taa" +
                    " ON a.id = taa.available_addresses_id AND ?10 = taa.transmitter_id" +
                    " WHERE a.country LIKE ?1 AND a.region LIKE ?2 AND a.city LIKE ?3 AND a.street LIKE ?4" +
                    " AND CAST (a.house AS TEXT) LIKE ?5 AND CAST (a.building AS TEXT) LIKE ?6 AND CAST (a.flat AS TEXT) LIKE ?7" +
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

    @Query(
            value = "SELECT COUNT(*) from address a" +
                    " INNER JOIN transmitter_available_addresses taa" +
                    " ON a.id = taa.available_addresses_id" +
                    " WHERE taa.transmitter_id = ?"
            , nativeQuery = true
    )
    Long countConnectedAddressByTransmitterId(Long transmitterId);
}
