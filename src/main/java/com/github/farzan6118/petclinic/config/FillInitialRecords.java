package com.github.farzan6118.petclinic.config;

import com.github.farzan6118.petclinic.model.*;
import com.github.farzan6118.petclinic.model.constant.Sex;
import com.github.farzan6118.petclinic.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class FillInitialRecords {

    @Bean
    CommandLineRunner fillInitialRecordsRunner(
            OwnerRepository ownerRepository,
            SpeciesRepository speciesRepository,
            PetRepository petRepository,
            RoomRepository roomRepository,
            RoomTypeRepository roomTypeRepository,
            VetRepository vetRepository,
            VetAvailabilityRepository vetAvailabilityRepository
    ) {
        return args -> fillInitialRecords(
                ownerRepository,
                speciesRepository,
                petRepository,
                roomRepository,
                roomTypeRepository,
                vetRepository,
                vetAvailabilityRepository
        );
    }

    @Transactional
    void fillInitialRecords(
            OwnerRepository ownerRepository,
            SpeciesRepository speciesRepository,
            PetRepository petRepository,
            RoomRepository roomRepository,
            RoomTypeRepository roomTypeRepository,
            VetRepository vetRepository,
            VetAvailabilityRepository vetAvailabilityRepository
    ) {
        if (ownerRepository.count() == 0
                && speciesRepository.count() == 0
                && petRepository.count() == 0
                && roomRepository.count() == 0
                && roomTypeRepository.count() == 0
                && vetRepository.count() == 0) {
            List<Species> species = speciesRepository.saveAll(List.of(
                    species("Dog", "DOG", "Domestic dog", "Common companion animal"),
                    species("Cat", "CAT", "Domestic cat", "Common companion animal"),
                    species("Rabbit", "RABBIT", "Domestic rabbit", "Small companion animal")
            ));

            List<Owner> owners = ownerRepository.saveAll(List.of(
                    owner("Mina", "Rahimi", "200000001", "09120000001", "mina.rahimi@example.com", "Tehran", "Valiasr Street"),
                    owner("Arman", "Karimi", "200000002", "09120000002", "arman.karimi@example.com", "Shiraz", "Zand Street"),
                    owner("Niloofar", "Ahmadi", "200000003", "09120000003", "niloofar.ahmadi@example.com", "Tabriz", "Shahrivar Street")
            ));

            petRepository.saveAll(List.of(
                    pet("Luna", "White", "Small black mark", Sex.FEMALE, species.get(0), owners.get(0), LocalDate.of(2021, 5, 12)),
                    pet("Milo", "Orange", "White paws", Sex.MAIL, species.get(1), owners.get(1), LocalDate.of(2022, 2, 8)),
                    pet("Coco", "Brown", "Long ears", Sex.FEMALE, species.get(2), owners.get(2), LocalDate.of(2023, 7, 21))
            ));

            List<RoomType> roomTypes = roomTypeRepository.saveAll(List.of(
                    roomType("Examination", "General examination room"),
                    roomType("Surgery", "Surgical procedure room"),
                    roomType("Isolation", "Isolation and observation room")
            ));

            roomRepository.saveAll(List.of(
                    room("Examination Room 1", "EXAM-01", roomTypes.get(0)),
                    room("Surgery Room 1", "SURG-01", roomTypes.get(1)),
                    room("Isolation Room 1", "ISO-01", roomTypes.get(2)),
                    room("Isolation Room 2", "ISO-02", roomTypes.get(2))
            ));

            Vet firstVet = vet("Sara", "Moradi", "09210000001", "sara.moradi@example.com", "100000001");
            firstVet.updateProfile(profile("Tehran", "Mirdamad Boulevard", "Internal medicine", LocalDate.of(1985, 3, 18)));

            Vet secondVet = vet("Reza", "Hosseini", "09210000002", "reza.hosseini@example.com", "100000002");
            secondVet.updateProfile(profile("Shiraz", "Maaliabad Street", "Surgery", LocalDate.of(1982, 11, 2)));

            Vet thirdVet = vet("Parisa", "Etemadi", "09210000003", "parisa.etemadi@example.com", "100000003");
            thirdVet.updateProfile(profile("Tabriz", "Ferdowsi Street", "Dermatology", LocalDate.of(1990, 6, 27)));

            vetRepository.saveAll(List.of(firstVet, secondVet, thirdVet));
        }

        List<Vet> vets = vetRepository.findAll();
        if (vetAvailabilityRepository.count() == 0 && vets.size() >= 3) {
            vetAvailabilityRepository.saveAll(List.of(
                    availability(vets.get(0), LocalDate.of(2026, 9, 7), LocalTime.of(9, 0), LocalTime.of(13, 0)),
                    availability(vets.get(1), LocalDate.of(2026, 9, 8), LocalTime.of(10, 0), LocalTime.of(14, 0)),
                    availability(vets.get(2), LocalDate.of(2026, 9, 9), LocalTime.of(8, 0), LocalTime.of(12, 0)),
                    availability(vets.get(1), LocalDate.of(2026, 9, 9), LocalTime.of(9, 30), LocalTime.of(12, 30))
            ));
        }
    }

    private Species species(String name, String code, String origin, String description) {
        Species species = new Species();
        species.setName(name);
        species.setCode(code);
        species.setOrigin(origin);
        species.setDescription(description);
        return species;
    }

    private Owner owner(
            String firstName,
            String lastName,
            String nationalId,
            String mobileNumber,
            String email,
            String city,
            String address
    ) {
        Owner owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setNationalId(nationalId);
        owner.setMobileNumber(mobileNumber);
        owner.setEmail(email);
        owner.setCity(city);
        owner.setAddress(address);
        return owner;
    }

    private Pet pet(
            String name,
            String color,
            String marks,
            Sex sex,
            Species species,
            Owner owner,
            LocalDate birthDate
    ) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setColor(color);
        pet.setMarks(marks);
        pet.setSex(sex);
        pet.setSpecies(species);
        pet.setOwner(owner);
        pet.setBirthDate(birthDate);
        return pet;
    }

    private RoomType roomType(String name, String description) {
        RoomType roomType = new RoomType();
        roomType.setName(name);
        roomType.setDescription(description);
        return roomType;
    }

    private Room room(String name, String code, RoomType roomType) {
        Room room = new Room();
        room.setName(name);
        room.setCode(code);
        room.setRoomType(roomType);
        room.setActive(true);
        return room;
    }

    private Vet vet(
            String firstName,
            String lastName,
            String mobileNumber,
            String email,
            String nationalId
    ) {
        Vet vet = new Vet();
        vet.setFirstName(firstName);
        vet.setLastName(lastName);
        vet.setMobileNumber(mobileNumber);
        vet.setEmail(email);
        vet.setNationalId(nationalId);
        return vet;
    }

    private Profile profile(String city, String address, String specialty, LocalDate birthDate) {
        Profile profile = new Profile();
        profile.setCity(city);
        profile.setAddress(address);
        profile.setSpecialty(specialty);
        profile.setBirthDate(birthDate);
        return profile;
    }

    private VetAvailability availability(
            Vet vet,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return VetAvailability.create(vet, date, startTime, endTime);
    }
}
