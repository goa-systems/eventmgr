DROP TABLE IF EXISTS `events_have_genres`;
--
DROP TABLE IF EXISTS `events`;
--
DROP TABLE IF EXISTS `genres`;
--
DROP TABLE IF EXISTS `locations`;
--
DROP TABLE IF EXISTS `preferences`;
--
CREATE TABLE `preferences` (
	`id` int unsigned AUTO_INCREMENT NOT NULL,
	`key` VARCHAR(64),
	`value` VARCHAR(255),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
CREATE TABLE `locations` (
	`id` bigint unsigned AUTO_INCREMENT NOT NULL,
	`name` VARCHAR(255),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
CREATE TABLE `genres` (
	`id` bigint unsigned AUTO_INCREMENT NOT NULL,
	`name` VARCHAR(255),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
CREATE TABLE `events` (
	`id` bigint unsigned AUTO_INCREMENT NOT NULL,
	`location_id` bigint unsigned NOT NULL,
	`start` TIMESTAMP NOT NULL,
	`end` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`),
	KEY `events_locations_fk` (`location_id`),
	CONSTRAINT `events_locations_fk` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
CREATE TABLE `events_have_genres` (
	`id` bigint unsigned AUTO_INCREMENT NOT NULL,
	`event_id` bigint unsigned NOT NULL,
	`genre_id` bigint unsigned NOT NULL,
	PRIMARY KEY (`id`),
	KEY `event_fk` (`event_id`),
	CONSTRAINT `event_fk` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
	KEY `genre_fk` (`genre_id`),
	CONSTRAINT `genre_fk` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
INSERT INTO `preferences` (`key`, `value`) VALUES ('dbversion', '0.0.1');
--
INSERT INTO `preferences` (`key`, `value`) VALUES ('dbenv', 'dev');