SELECT
	`events`.`id`,
	`events`.`location_id`,
	`events`.`start`,
	`events`.`end`,
	`locations`.`id`,
	`locations`.`name`,
	`genres`.`id`,
	`genres`.`name`
FROM
	`events`
LEFT OUTER JOIN `locations` ON
	`events`.`location_id` = `locations`.`id`
LEFT OUTER JOIN `events_have_genres` ON
	`events`.`id` = `events_have_genres`.`event_id`
LEFT OUTER JOIN `genres` ON
	`events_have_genres`.`genre_id` = `genres`.`id`