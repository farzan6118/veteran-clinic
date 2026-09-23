package com.github.farzan6118.petclinic.common.persistence;

import com.github.farzan6118.petclinic.common.enums.Sex;
import com.github.farzan6118.petclinic.owner.model.Owner;
import com.github.farzan6118.petclinic.owner.repository.OwnerRepository;
import com.github.farzan6118.petclinic.pet.model.Pet;
import com.github.farzan6118.petclinic.pet.model.Species;
import com.github.farzan6118.petclinic.pet.repository.PetRepository;
import com.github.farzan6118.petclinic.pet.repository.SpeciesRepository;
import com.github.farzan6118.petclinic.room.model.Room;
import com.github.farzan6118.petclinic.room.model.RoomType;
import com.github.farzan6118.petclinic.room.repository.RoomRepository;
import com.github.farzan6118.petclinic.room.repository.RoomTypeRepository;
import com.github.farzan6118.petclinic.vet.model.Profile;
import com.github.farzan6118.petclinic.vet.model.Vet;
import com.github.farzan6118.petclinic.vet.model.VetAvailability;
import com.github.farzan6118.petclinic.vet.repository.VetAvailabilityRepository;
import com.github.farzan6118.petclinic.vet.repository.VetRepository;
import com.github.farzan6118.petclinic.visit.model.DurationTemplate;
import com.github.farzan6118.petclinic.visit.repository.DurationTemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            VetAvailabilityRepository vetAvailabilityRepository,
            DurationTemplateRepository durationTemplateRepository

    ) {
        return args -> fillInitialRecords(
                ownerRepository,
                speciesRepository,
                petRepository,
                roomRepository,
                roomTypeRepository,
                vetRepository,
                vetAvailabilityRepository,
                durationTemplateRepository
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
            VetAvailabilityRepository vetAvailabilityRepository,
            DurationTemplateRepository durationTemplateRepository
    ) {
        if (ownerRepository.count() == 0
                && speciesRepository.count() == 0
                && petRepository.count() == 0
                && roomRepository.count() == 0
                && roomTypeRepository.count() == 0
                && vetRepository.count() == 0
                && durationTemplateRepository.count() == 0) {

            List<DurationTemplate> durationTemplate = durationTemplateRepository.saveAll(List.of(
                    durationTemplate("QUICK", 15, "Quick visit"),
                    durationTemplate("SHORT", 20, "Short visit"),
                    durationTemplate("STANDARD", 30, "Standard visit"),
                    durationTemplate("EXTENDED", 45, "Extended visit"),
                    durationTemplate("LONG", 60, "Long visit"),
                    durationTemplate("VERY_LONG", 120, "Very long visit")
            ));

            List<Species> species = speciesRepository.saveAll(List.of(
                    species("Dog", "DOG", "Canis lupus familiaris",
                            "Common companion animal."),
                    species("Cat", "CAT", "Felis catus",
                            "Common companion animal."),
                    species("Rabbit", "RABBIT", "Oryctolagus cuniculus",
                            "Common companion animal."),
                    species("Hamster", "HAMSTER", "Cricetinae",
                            "Small companion animal."))
            );

            List<Owner> owners = ownerRepository.saveAll(List.of(
                    owner("Ms.", "Mina", "Rahimi", "200000001", LocalDate.of(1990, 4, 12), "09120000001", "mina.rahimi@example.com", "Tehran", "Valiasr Street"),
                    owner("Mr.", "Arman", "Karimi", "200000002", LocalDate.of(1987, 9, 25), "09120000002", "arman.karimi@example.com", "Shiraz", "Zand Street"),
                    owner("Ms.", "Niloofar", "Ahmadi", "200000003", LocalDate.of(1995, 1, 8), "09120000003", "niloofar.ahmadi@example.com", "Tabriz", "Shahrivar Street")
            ));

            petRepository.saveAll(List.of(
                    pet("Luna", "White", "Small black mark", Sex.FEMALE, species.get(0), owners.get(0), LocalDate.of(2021, 5, 12)),
                    pet("Milo", "Orange", "White paws", Sex.MALE, species.get(1), owners.get(1), LocalDate.of(2022, 2, 8)),
                    pet("Coco", "Brown", "Long ears", Sex.FEMALE, species.get(2), owners.get(2), LocalDate.of(2023, 7, 21))
            ));

            List<RoomType> roomTypes = roomTypeRepository.saveAll(List.of(
                    roomType(
                            "Examination",
                            "General-purpose veterinary examination room used for routine physical examinations, "
                                    + "follow-up visits, illness assessment, consultations, vaccinations, injections, "
                                    + "basic diagnostic procedures, and other non-surgical patient assessments."
                    ),
                    roomType(
                            "Treatment",
                            "Veterinary treatment room used for non-surgical procedures and patient care such as "
                                    + "wound care, bandaging, injections, fluid therapy, medication administration, "
                                    + "minor procedures, and routine treatments that do not require a surgical suite."
                    ),
                    roomType(
                            "Surgery",
                            "Dedicated veterinary surgical room used for surgical procedures and other invasive interventions "
                                    + "that require a controlled and appropriately equipped surgical environment."
                    ),
                    roomType(
                            "Dental",
                            "Dedicated veterinary dental treatment room used for dental examinations, dental cleaning, "
                                    + "oral procedures, dental surgery, and other procedures involving the teeth and oral cavity."
                    ),
                    roomType(
                            "Imaging",
                            "Diagnostic imaging room used for veterinary imaging procedures such as X-ray, ultrasound, "
                                    + "and other diagnostic imaging examinations requiring dedicated imaging equipment."
                    ),
                    roomType(
                            "Physiotherapy",
                            "Veterinary rehabilitation room used for physiotherapy, physical rehabilitation, mobility exercises, "
                                    + "post-operative rehabilitation, and other non-surgical rehabilitation treatments."
                    ),
                    roomType(
                            "Isolation",
                            "Dedicated isolation room used for patients suspected or confirmed to have contagious or infectious "
                                    + "conditions and requiring separation from other patients for infection-control purposes."
                    )
            ));

            roomRepository.saveAll(List.of(
                    room("Examination Room 1", "EXAM-01", roomTypes.getFirst()),
                    room("Examination Room 2", "EXAM-02", roomTypes.getFirst()),
                    room("Examination Room 3", "EXAM-03", roomTypes.getFirst()),

                    room("Treatment Room 1", "TREAT-01", roomTypes.get(1)),
                    room("Treatment Room 2", "TREAT-02", roomTypes.get(1)),

                    room("Surgery Room 1", "SURG-01", roomTypes.get(2)),
                    room("Surgery Room 2", "SURG-02", roomTypes.get(2)),

                    room("Dental Room 1", "DENT-01", roomTypes.get(3)),

                    room("Imaging Room 1", "IMG-01", roomTypes.get(4)),

                    room("Physiotherapy Room 1", "PHYSIO-01", roomTypes.get(5)),

                    room("Isolation Room 1", "ISO-01", roomTypes.get(6)),
                    room("Isolation Room 2", "ISO-02", roomTypes.get(6))
            ));

            Vet firstVet = vet("Dr.", "Sara", "Moradi", "09210000001", "sara.moradi@example.com", "100000001");
            firstVet.updateProfile(profile("Tehran", "Mirdamad Boulevard", "Internal medicine", LocalDate.of(1985, 3, 18)));

            Vet secondVet = vet("Vet.", "Reza", "Hosseini", "09210000002", "reza.hosseini@example.com", "100000002");
            secondVet.updateProfile(profile("Shiraz", "Maaliabad Street", "Surgery", LocalDate.of(1982, 11, 2)));

            Vet thirdVet = vet("Dr.", "Parisa", "Etemadi", "09210000003", "parisa.etemadi@example.com", "100000003");
            thirdVet.updateProfile(profile("Tabriz", "Ferdowsi Street", "Dermatology", LocalDate.of(1990, 6, 27)));

            vetRepository.saveAll(List.of(firstVet, secondVet, thirdVet));
        }
        List<Vet> vets = vetRepository.findAll();
        if (vetAvailabilityRepository.count() == 0 && vets.size() >= 3) {
            LocalDate today = LocalDate.now();

            vetAvailabilityRepository.saveAll(List.of(
                    availability(
                            vets.get(0),
                            today.plusDays(1).atTime(9, 0),
                            today.plusDays(1).atTime(13, 0)
                    ),
                    availability(
                            vets.get(0),
                            today.plusDays(2).atTime(9, 0),
                            today.plusDays(2).atTime(12, 20)
                    ),
                    availability(
                            vets.get(1),
                            today.plusDays(1).atTime(10, 0),
                            today.plusDays(1).atTime(14, 0)
                    ),
                    availability(
                            vets.get(1),
                            today.plusDays(2).atTime(9, 30),
                            today.plusDays(2).atTime(12, 30)
                    ),
                    availability(
                            vets.get(1),
                            today.plusDays(3).atTime(9, 30),
                            today.plusDays(3).atTime(12, 30)
                    ),
                    availability(
                            vets.get(2),
                            today.plusDays(1).atTime(8, 0),
                            today.plusDays(1).atTime(12, 0)
                    ),
                    availability(
                            vets.get(2),
                            today.plusDays(2).atTime(9, 30),
                            today.plusDays(2).atTime(14, 0)
                    ),
                    availability(
                            vets.get(2),
                            today.plusDays(3).atTime(9, 30),
                            today.plusDays(3).atTime(14, 0)
                    )
            ));
        }

        ownerRepository.findAll().forEach(owner -> {
            if (owner.getBirthDate() == null) {
                owner.setBirthDate(switch (owner.getEmail()) {
                    case "mina.rahimi@example.com" -> LocalDate.of(1990, 4, 12);
                    case "arman.karimi@example.com" -> LocalDate.of(1987, 9, 25);
                    case "niloofar.ahmadi@example.com" -> LocalDate.of(1995, 1, 8);
                    default -> null;
                });
            }
        });
    }

    private DurationTemplate durationTemplate(String name, Integer durationMinutes, String description) {
        DurationTemplate template = new DurationTemplate();
        template.setName(name);
        template.setDurationMinutes(durationMinutes);
        template.setDescription(description);
        return template;
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
            String title,
            String firstName,
            String lastName,
            String nationalId,
            LocalDate birthDate,
            String mobileNumber,
            String email,
            String city,
            String address
    ) {
        Owner owner = new Owner();
        owner.setTitle(title);
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setNationalId(nationalId);
        owner.setBirthDate(birthDate);
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
            String title,
            String firstName,
            String lastName,
            String mobileNumber,
            String email,
            String nationalId
    ) {
        Vet vet = new Vet();
        vet.setTitle(title);
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
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return new VetAvailability().create(vet, startTime, endTime);
    }
}
