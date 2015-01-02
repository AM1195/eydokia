INSERT INTO `eydokia`.`role` (`role_id`, `name`) VALUES ('1', 'guest');
INSERT INTO `eydokia`.`role` (`role_id`, `name`) VALUES ('2', 'registered');
INSERT INTO `eydokia`.`role` (`role_id`, `name`) VALUES ('3', 'moderator');
INSERT INTO `eydokia`.`role` (`role_id`, `name`) VALUES ('4', 'sys_admin');

INSERT INTO `schedule_type` (`schedule_type_id`,`name`) VALUES (1,'none');
INSERT INTO `schedule_type` (`schedule_type_id`,`name`) VALUES (2,'weekly');
INSERT INTO `schedule_type` (`schedule_type_id`,`name`) VALUES (3,'monthly/day');
INSERT INTO `schedule_type` (`schedule_type_id`,`name`) VALUES (4,'monthly/week');
INSERT INTO `schedule_type` (`schedule_type_id`,`name`) VALUES (5,'yearly');

INSERT INTO `eydokia`.`room` (`room_id`, `disabled`, `name`, `description`) VALUES ('1', 0, 'Aithousa A', 'prwti aithousa');
INSERT INTO `eydokia`.`room` (`room_id`, `disabled`, `name`, `description`) VALUES ('2', 0, 'Aithousa B', 'deuteri aithousa');
INSERT INTO `eydokia`.`room` (`room_id`, `disabled`, `name`, `description`) VALUES ('3', 0, 'Aithousa teletwn', 'triti aithousa');
INSERT INTO `eydokia`.`room` (`room_id`, `disabled`, `name`, `description`) VALUES ('4', 1, 'Palia aithousa', 'tetarti aithousa');

INSERT INTO `eydokia`.`user` (`user_id`, `role_id`, `email`, `full_name`, `phone`, `username`, `password`, `extra_info`) VALUES ('-1', '1', 'no@email.com', 'Guest', '6900000000', 'guest', 'guest', 'Hi! I\'m guest');
INSERT INTO `eydokia`.`user` (`user_id`, `role_id`, `email`, `full_name`, `phone`, `username`, `password`, `extra_info`) VALUES ('1', '4', 'devn@hotmail.gr', 'Nilos Psathas', '6978660917', 'admin', 'admin', 'Call me when you have a problem');
INSERT INTO `eydokia`.`user` (`user_id`, `role_id`, `email`, `full_name`, `phone`, `username`, `password`, `extra_info`) VALUES ('2', '3', 'npsathas@uthl.gr', 'Nilos Psathas', '6978660917', 'moderator', 'moderator', 'Is all right here?');
INSERT INTO `eydokia`.`user` (`user_id`, `role_id`, `email`, `full_name`, `phone`, `username`, `password`, `extra_info`) VALUES ('3', '2', 'nilos.nilos@hotmail.com', 'Neilos Ps', '2421055644', 'neilos', '1234', 'My name is Neilos');
INSERT INTO `eydokia`.`user` (`user_id`, `role_id`, `email`, `full_name`, `phone`, `username`, `password`, `extra_info`) VALUES ('4', '2', 'nilos.nilos@gmail.com', 'N Psathas', '2410941533', 'nilos', '1234', 'Whats up, doc? :p');

INSERT INTO `eydokia`.`schedule` (`schedule_id`, `schedule_type_id`, `weak_day`, `month_day`, `month_weak`, `year_day`) VALUES ('1', '1', '0', '0', '0', '0');
INSERT INTO `eydokia`.`schedule` (`schedule_id`, `schedule_type_id`, `weak_day`, `month_day`, `month_weak`, `year_day`) VALUES ('2', '2', '3', '0', '0', '0');
INSERT INTO `eydokia`.`schedule` (`schedule_id`, `schedule_type_id`, `weak_day`, `month_day`, `month_weak`, `year_day`) VALUES ('3', '3', '0', '17', '0', '0');
INSERT INTO `eydokia`.`schedule` (`schedule_id`, `schedule_type_id`, `weak_day`, `month_day`, `month_weak`, `year_day`) VALUES ('4', '4', '2', '0', '1', '0');
INSERT INTO `eydokia`.`schedule` (`schedule_id`, `schedule_type_id`, `weak_day`, `month_day`, `month_weak`, `year_day`) VALUES ('5', '5', '0', '0', '0', '112');

INSERT INTO `eydokia`.`entry` (`entry_id`, `user_id`, `room_id`, `schedule_id`, `status`, `start_date`, `start_time`, `end_date`, `end_time`, `description`) VALUES ('1', '3', '1', '1', '0', '2014-12-31', '10:00:00', '2014-12-31', '10:45:00', 'Dialeksi gia ton Agio Vasili');
INSERT INTO `eydokia`.`entry` (`entry_id`, `user_id`, `room_id`, `schedule_id`, `status`, `start_date`, `start_time`, `end_date`, `end_time`, `description`, `info_user`, `info_text`) VALUES ('2', '3', '1', '2', '1', '2015-01-07', '08:00:00', '2015-06-20', '10:15:00', 'Prwino evdomadiaio mathima kathe Tetarti', '', '');
INSERT INTO `eydokia`.`entry` (`entry_id`, `user_id`, `room_id`, `schedule_id`, `status`, `start_date`, `start_time`, `end_date`, `end_time`, `description`) VALUES ('3', '4', '2', '3', '1', '2015-01-17', '14:00:00', '2015-06-20', '15:00:00', 'Miniaia mesimeriani sunantisi kathe 17 tou minos');
INSERT INTO `eydokia`.`entry` (`entry_id`, `user_id`, `room_id`, `schedule_id`, `status`, `start_date`, `start_time`, `end_date`, `end_time`, `description`) VALUES ('4', '4', '3', '4', '1', '2015-06-02', '11:45:00', '2015-12-25', '12:45:00', 'Miniaia sunantisi kathe 1h evdomada tou minos k mera triti');
INSERT INTO `eydokia`.`entry` (`entry_id`, `user_id`, `room_id`, `schedule_id`, `status`, `start_date`, `start_time`, `end_date`, `end_time`, `description`, `info_user`, `info_text`, `info_time`) VALUES ('5', '4', '4', '5', '-1', '2015-02-12', '13:15:00', '2019-02-12', '22:15:00', 'Etisia ekdilosi, kopi ti pita. Me mpoufedes k pota. Akolouthei vareti omilia.', 'Nilos Psathas [admin]', 'Sorry its my birthdate', '2014-12-30 15:00:00');

