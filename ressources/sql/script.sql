CREATE TABLE `TEACHERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `SESSIONS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50),
  `description` VARCHAR(2000),
  `date` TIMESTAMP,
  `teacher_id` int,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `USERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `admin` BOOLEAN NOT NULL DEFAULT false,
  `email` VARCHAR(255),
  `password` VARCHAR(255),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `PARTICIPATE` (
  `user_id` INT, 
  `session_id` INT
);

ALTER TABLE `SESSIONS` ADD FOREIGN KEY (`teacher_id`) REFERENCES `TEACHERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`session_id`) REFERENCES `SESSIONS` (`id`);

INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');


INSERT INTO `users` (`id`, `last_name`, `first_name`, `admin`, `email`, `password`, `created_at`, `updated_at`) VALUES
(1, 'Admin', 'Admin', 1, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq', '2024-05-27 19:10:42', '2024-05-27 21:10:42'),
(2, 'Berteau', 'Thomas', 0, 'thomas.berteau@test.com', '$2a$10$q.pjDjP1CubegDC6S8W6oeR.qiUMH7HV.5dmIH5iRUT9SKP8tjRRy', '2024-05-30 04:42:11', '2024-05-30 06:42:11'),
(3, 'Mnt', 'Lea', 0, 'lea.mnt@test.com', '$2a$10$thZeaKDf7pBAcaICpGAIIunUD3zQsZerVey67f26NR7Gwi3XfAOhe', '2024-05-30 04:43:31', '2024-05-30 06:43:31'),
(4, 'Kai', 'Kaya', 0, 'kaya.kai@test.com', '$2a$10$dePPLQZm9QYAeKJKOnD9ceBGM08Atx1UY39aS/YP9TLo0.h.TyMjG', '2024-05-30 04:43:52', '2024-05-30 06:43:52'); 

INSERT INTO `sessions` (`id`, `name`, `description`, `date`, `teacher_id`, `created_at`, `updated_at`) VALUES
(1, 'Session d\'Initiation', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.', '2024-05-30 00:00:00', 1, '2024-05-30 04:41:16', '2024-05-30 06:45:07'),
(2, 'Session Intermédiaire', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.', '2024-05-31 00:00:00', 1, '2024-05-30 04:41:30', '2024-05-30 06:41:30'),
(3, 'Session Avancée', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.', '2024-06-01 00:00:00', 2, '2024-05-30 04:41:45', '2024-05-30 06:41:45');

INSERT INTO `participate` (`user_id`, `session_id`) VALUES
(3, 2),
(2, 1),
(4, 3);