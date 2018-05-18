USE keyist;

SET FOREIGN_KEY_CHECKS = 0;


DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(500) NOT NULL,
  `password` varchar(500) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `city` varchar(90) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `zip` varchar(12) DEFAULT NULL,
  `email_verified` tinyint(1) DEFAULT '0',
  `registration_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `phone` varchar(20) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `address2` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--------------------------------------------------------

DROP TABLE IF EXISTS `verification_token`;

CREATE TABLE `verification_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `expiry_date` TIMESTAMP NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--------------------------------------------------------

DROP TABLE IF EXISTS `password_reset_token`;

CREATE TABLE `password_reset_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `expiry_date` TIMESTAMP NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE(`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--------------------------------------------------------

DROP TABLE IF EXISTS `email_reset_token`;

CREATE TABLE `email_reset_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `expiry_date` TIMESTAMP NOT NULL,
  `user_id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE(`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `product_category`;

CREATE TABLE IF NOT EXISTS `product_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE(`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1  ;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `product`;

CREATE TABLE IF NOT EXISTS `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sku` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` float(100,2) NOT NULL,
  `cargo_price` float(100,2) NOT NULL,
  `tax_percent` float(100,2) NOT NULL,
  `cart_desc` varchar(250) NOT NULL,
  `long_desc` text NOT NULL,
  `thumb` varchar(250) DEFAULT NULL,
  `image` varchar(250) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `stock` float DEFAULT NULL,
  `sell_count` int DEFAULT 0,
  `live` tinyint(1) DEFAULT 0,
  `unlimited` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`category_id` ) REFERENCES `product_category` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `discount`;

CREATE TABLE IF NOT EXISTS `discount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(240) NOT NULL,
  `discount_percent` int(3) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE(`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

INSERT INTO  `discount`(`id`,`code`,`discount_percent`) VALUES(1,'WLCM_2018',20);

-- --------------------------------------------------------


DROP TABLE IF EXISTS `orders`;


CREATE TABLE IF NOT EXISTS `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ship_name` varchar(100) NOT NULL,
  `ship_address` varchar(100) NOT NULL,
  `ship_address2` varchar(100) DEFAULT NULL,
  `city` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `zip` varchar(20) DEFAULT NULL,
  `country` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `total_price` float NOT NULL,
  `total_cargo_price` float NOT NULL,
  `discount_id` int(11) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `date` timestamp NOT NULL,
  `shipped` tinyint(1) NOT NULL DEFAULT '0',
  `cargo_firm` varchar(100),
  `tracking_number` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id` ) REFERENCES `user` (`id`),
  FOREIGN KEY (`discount_id` ) REFERENCES `discount` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE IF NOT EXISTS `order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`order_id` ) REFERENCES `orders` (`id`),
  FOREIGN KEY (`product_id` ) REFERENCES `product` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


-- --------------------------------------------------------


DROP TABLE IF EXISTS `cart`;

CREATE TABLE IF NOT EXISTS `cart` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `discount_id` int(11) DEFAULT NULL,
  `total_price` float NOT NULL DEFAULT 0,
  `total_cargo_price` float NOT NULL DEFAULT 0,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id` ) REFERENCES `user` (`id`),
  FOREIGN KEY (`discount_id` ) REFERENCES `discount` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


-- --------------------------------------------------------

DROP TABLE IF EXISTS `cart_item`;

CREATE TABLE IF NOT EXISTS `cart_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cart_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `amount` int NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`cart_id` ) REFERENCES `cart` (`id`),
  FOREIGN KEY (`product_id` ) REFERENCES `product` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;




