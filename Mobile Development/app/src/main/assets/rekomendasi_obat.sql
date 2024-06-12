-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 05, 2024 at 10:41 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `obatpenyakitkulit`
--

-- --------------------------------------------------------

--
-- Table structure for table `obat`
--

CREATE TABLE `obat` (
  `nama_penyakit` varchar(200) NOT NULL,
  `obat_rekomendasi` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `obat`
--

INSERT INTO `obat` (`nama_penyakit`, `obat_rekomendasi`) VALUES
('athlete foot', 'Salep Kulit 88, Ketoconazole Cream, Miconazole Cream, Canesten, Daktarin, Fungiderm, atau Kalpanax.'),
('vasculitis', 'cyclophosphamide atau azathioprine, prednisone atau methylprednisolone '),
('ringworm', 'New Astar Cream, Formyco, Zoralin, Mycoral, dan Canesten'),
('atopic dermatitis', 'Kenacort, Bufacomb, Esperson, Pharmason, dan Desoximetasone'),
('herpes hpv and other std', 'Glizigen Spray, Clinovir Cream, dan Zovirax Cream'),
('impetigo', 'Bactoderm Cream, Pirotop Cream, Medi-Klin Gel, Diprogenta Cream, Benoson G Cream, Sagestam Cream, Salticin Cream, Gentasolon Cream, Afucid Cream, Chloramfecort H Cream,.'),
('custineous larva margins', 'Albendazole , Ivermectin, Tiabendazole'),
('eczema', 'Clobetasol, Kloderma, Diprosone, Ikaderm, Elopro Salep, '),
('chickenpox', 'CTM, Mederma Proaktif, Ozen, Clinovir, atau Herclov.'),
('tinea ringworm candidiasis and other', 'Daktarin, Canesten, Kalpanax, Fungiderm, Mycoral, Salep Kulit 88, Myco-Z, Trosyd'),
('systemic disease', 'NSAID, Kortikosteroid, Hydroxycloroquine, imunosupresan, Rituximab '),
('urticaria hives', 'loratadine, cetirizine, dan fexofenadine, chlorperamine, dypenhidramine,prednisolone, cimetidine, famotidine dan ranitidine, Omaluzimab, Siklosporin'),
('vascular tumors', 'Kortikosteroid, Antikonvulsan, Antiemetik, Lomustine, Temozolomide, Bevacizumab, Everolimus'),
('shingles', 'acyclovir, famciclovir, dan valacyclovir. acyclovir, callusol, kutilos'),
('seborrheic keratoses and other benign t', 'Retinoid (tretinoin), 5-Fluorouracil (5-FU) topikal'),
('psoriasis pictures lichen planus ', 'beclometasone topikal, Antihistamin, azathioprine, mycophenolate, cyclosporine, dan methotrexate, isotretinoin atau acitretin, '),
('poiston ivy', 'Antibiotik, calamine, prednisone'),
('nail fungus', 'Emtrix Gel, Fungistop, Fungiderm Cream, Mustika Ratu Minyak Zaitun, serta Diflucan'),
('melanoma skin cancer', 'Pembrolizumab, Atezolizumab, Vemurafenib, imatinib, nilotinib'),
('lupus and other connective tissue disease', 'Voltaren® Celebrex® dan Orudis®, Plaquenil®, azathioprine, cyclophosphamide dan methotrexate'),
('light diseases and disorders of pigmentation ', 'Obat oles retinoid , Obat oles hydroquinone, '),
('cellulitis impetigo and other bacterial infection', 'penisilin, clindamycin, sefalosporin, dan makrolid,antibiotik dikloksasilin, sefaleksin, trimetoprim dengan sulfametoksazol, klindamisin, atau doksisiklin'),
('Wart Molluscum and other viral infection', 'kalusol, acyclovir, callusol, kutilos'),
('actinic keratosis basal cell', 'fluorouracil, imiquimod, ingenolmebutate, dan diclofenac');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
