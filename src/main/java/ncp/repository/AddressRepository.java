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
    Long isExistsFlats(String country, String region, String city, String street, long house, long building);

    @Query(
            value = "SELECT COUNT(*) FROM address a WHERE a.country =?1 AND a.region =?2 AND a.city=?3 AND a.street=?4" +
                    " AND a.house=?5 AND a.building IS NOT NULL"
            , nativeQuery = true
    )
    Long isExistsBuildings(String country, String region, String city, String street, long house);

    @Query(
            value = "SELECT COUNT(*) FROM address a WHERE a.country =?1 AND a.region =?2 AND a.city=?3 AND a.street IS NOT NULL"
            , nativeQuery = true
    )
    Long isExistsStreets(String country, String region, String city);

    @Query(
            value = "SELECT COUNT(*) FROM address a WHERE a.country =? AND a.region IS NOT NULL"
            , nativeQuery = true
    )
    Long isExistsRegions(String country);
}
