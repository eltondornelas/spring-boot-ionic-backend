package com.esd.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.esd.cursomc.services.exceptions.FileException;

@Service
public class ImageService {
	// servico responsável por exercer funcionalidades de imagem

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		// esse comando pega a extensão do arquivo que veio como parâmetro no método

		if (!"png".equals(ext) && !"jpg".equals(ext)) {
			// se a extensão não for nem png e nem jpg ele vai dar exceção.
			throw new FileException("Somente imagens PNG e JPG são permitidas");
		}

		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());

			if ("png".equals(ext)) {
				img = pngToJpg(img);
				// como só queremos imagem jpg, mas aceitamos receber uma png, precisamos
				// confirmar se é png para converter para jpg
			}

			return img;

		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		// alguns ong tem fundo transparente, então ele preenche com branco para
		// pradonizar
		return jpgImage;
	}

	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public BufferedImage cropSquare(BufferedImage sourceImg) {
		//"cropar" = cortar
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		//aqui é pra ele descobrir qual o mínimo
		
		return Scalr.crop(sourceImg,
				(sourceImg.getWidth() / 2) - (min / 2),
				(sourceImg.getHeight() / 2) - (min / 2),
				min,
				min);
	}

	public BufferedImage resize(BufferedImage sourceImg, int size) {
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
		//Ultra quality para evitar o máixmo a perda de qualidade
	}
}
