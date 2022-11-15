INSERT INTO genres (id, name) VALUES(1, 'queerbeet');
--
INSERT INTO genres (id, name) VALUES(2, 'rock');
--
INSERT INTO genres (id, name) VALUES(3, 'punk');
--
INSERT INTO genres (id, name) VALUES(4, 'metal');
--
INSERT INTO genres (id, name) VALUES(5, 'hiphop');
--
INSERT INTO genres (id, name) VALUES(6, 'dancehall');
--
INSERT INTO genres (id, name) VALUES(7, '80ties');
--
INSERT INTO genres (id, name) VALUES(8, '90ties');
--
INSERT INTO genres (id, name) VALUES(9, '2000s');
--
INSERT INTO locations (id, name) VALUES(1, 'Bar 1');
--
INSERT INTO events (id, location_id, `start`, `end`) VALUES(1, 1, '2022-10-21 20:00:00', '2022-10-22 02:00:00');
--
INSERT INTO events_have_genres (id, event_id, genre_id) VALUES(4, 1, 1);
--
INSERT INTO events_have_genres (id, event_id, genre_id) VALUES(1, 1, 2);
--
INSERT INTO events_have_genres (id, event_id, genre_id) VALUES(2, 1, 3);
--
INSERT INTO events_have_genres (id, event_id, genre_id) VALUES(3, 1, 4);