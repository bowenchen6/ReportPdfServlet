package com.bowen.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bowen.bean.Score;
import com.bowen.bean.Student;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil {
	// ����pdf�ļ���·��
	private String path = "E:\\pdf\\2017_6_27\\";
	// ����ͼƬ��·��
	public String imagePath = "E:\\image\\1.png";

	public void createPdf() {
		// ��������
		Student student = new Student();
		student.setId(1);
		student.setName("bowenchen");
		student.setAge(24);
		student.setSex("��");
		student.setAddress("�Ϻ����ֶ������ֶ����԰");
		File file = new File(path);
		// ���ڴ������ļ�·�� ����ʱ����Ҫ����·��
		if (!file.exists()) {
			file.mkdirs();
		}
		// ��ȡ����pdf�ļ��ĵ�ַ
		String filePath = getSurveyInvoicePdf(path, student);
		System.out.println("��ȡ�ĵ�ַ�ǣ�"+filePath);
	}
	public Image getImageObject(String imagePath) {
		Image imageObject = null;
		// ͨ��ͼƬ·����ȡͼƬ����
		try {
			imageObject = Image.getInstance(imagePath);

		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
		return imageObject;

	}
	//����ͼƬ�ĸ߶ȺͿ�Ȼ�ȡռ�ݵİٷֱ���
	private int getPercent(float h, float w) {
		int p = 0;
		float p2 = 0.0f;
		p2 = 595 / w * 100;
		p = Math.round(p2);
		return p;
	}
	//����������Ϣ���ı���ʽ
	public void setText(PdfWriter writer, String content,Student student, float x, float y, float z,boolean flag) throws DocumentException, IOException {
		//ʹ��pdfContentByte����һϵ�е���ӳ�䵽ÿһ���������ķ���,������������Adobe�ĳ���ģ�͡������Ҳ�кܶ�����ķ�����������,Բ�Ρ����κ��ı��ھ���λ�á�
		//��ȡ��
		PdfContentByte canvas=writer.getDirectContent();
		//�������壬ʹ��windos�Դ�����������
		BaseFont bfChinese=BaseFont.createFont();
		//BaseFont bfChinese= BaseFont.createFont("C:/WINDOWS/Fonts/smalle.fon", BaseFont.COURIER_BOLD,BaseFont.EMBEDDED); 
		//���������������������
		Font fontChinese = new Font(bfChinese, 8,Font.BOLD);
		//Phrase������������һ���̾�,������֪��������������֮��ļ��
		Phrase phrase=new Phrase(content,fontChinese);
		//ѧ��ѧ����ʾʹ��10������
		Font  id_Font=new Font(bfChinese,10,Font.BOLD);
		Phrase id_Phrase=new Phrase(student.getId().toString(),id_Font);
		//�����ѧ�����ó�10�����壬��������ѧ����Ϣ���������ó�8������
		if(flag){
			//��pdf�������ˮӡ x:�����x���� y:�����y����,z:�ı�����ת�Ƕ�
			ColumnText.showTextAligned(canvas,Element.ALIGN_UNDEFINED,id_Phrase,x,y,z);
		}else{
			//��pdf�������ˮӡ x:�����x���� y:�����y����,z:�ı�����ת�Ƕ�
			ColumnText.showTextAligned(canvas, Element.ALIGN_UNDEFINED, phrase, x, y, z);
		}
		
	}
	//�����ʽ������
	private void setTableText(PdfWriter writer,String text,Font font,float x, float y){
		//����һ����һ�еı��
		PdfPTable table=new PdfPTable(1);
		///���ñ����ܿ��
		table.setTotalWidth(120);
		//�����Ŀ������
		table.setLockedWidth(true);
		//��������һ����Ԫ��
		PdfPCell cell=new PdfPCell(new Phrase(text,font));
		//���õ�Ԫ�����ʽ
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorderWidth(0);
		cell.setPaddingRight(5);
		//����Ԫ����ӵ������
		table.addCell(cell);
		//public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas); 
        //����rowStart�����뿪ʼ���е���Ŀ������rowEnd��������ʾ�������У����������ʾ���е��У���-1����xPos��yPos�Ǳ������꣬canvas��һ��PdfContentByte����
		//���ñ�����
		table.writeSelectedRows(0,+1,x,y,writer.getDirectContent());
	}

	// ��ȡ�������pdf���ļ�·��
	/*
	 * �ٽ���com.lowagie.text.Document�����ʵ����
	����Document document = new Document(); 
	
	�����ڽ���һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽�����С�
	����PDFWriter.getInstance(document, new FileOutputStream("Helloworld.PDF")); 
	
	�����۴��ĵ���
	����document.open(); 
	
	���������ĵ���������ݡ�
	����document.add(new Paragraph("Hello World")); 
	
	�����ݹر��ĵ���
	����document.close(); 
	 */
	public String getSurveyInvoicePdf(String path, Student student) {
		//�����pdf�ļ���·��
		List<String> filePathList=new ArrayList<>();
		//pdf���·��
		String filePath= path+"test.pdf";
		Image imageObject = null;
		if (null != getImageObject(imagePath)) {
			imageObject = getImageObject(imagePath);
		} else {
			System.out.println("�Ҳ���ͼƬ��");
		}
		//����document����
	   Document document=new Document(PageSize.A4, 0, 0, 0, 0);
	   try {
		   //����һ����д��
		   PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(filePath));
		   //���ĵ�
		   document.open();
		   //���ĵ����������
		   //��ҳ
		   document.newPage();
		   //����ͼƬ��ס��ʾ
		   imageObject.setAlignment(Image.MIDDLE);
		   imageObject.setAlignment(Image.TEXTWRAP);
		   //��ȡͼƬ�ĸ߶�
		   float heigth=imageObject.getHeight();
		   //��ȡͼƬ�Ŀ��
		   float width=imageObject.getWidth();
		   //����ͼƬ�Ĵ�С��ȡͼƬռ�ݵı���
		   int percent=this. getPercent(heigth,width);
		   //����ͼƬ�ķ�������
		   imageObject.scalePercent(percent);
		   //�����ú�ı���ͼƬ��ӵ�pdf�ļ���
		   document.add(imageObject);
		   //�������ƫ����
			int data_X=0;
			int data_Y=1;
		   //ѧ��������Ϣ������ʽ����
			this.setText(writer, student.getName(),student, 498+data_X, 795+data_Y, 0,false);
			this.setText(writer, student.getAge().toString(),student, 498+data_X, 772+data_Y, 0,false);
			this.setText(writer, student.getSex(),student, 62+data_X, 749+data_Y, 0,false);
			this.setText(writer, student.getAddress(),student,115+data_X, 134+data_Y, 0,false);
			//���ϽǴ����ѧ��
			this.setText(writer,student.getId().toString(), student, 500,820,0,true);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse("2008-08-08 12:10:12");
			//ѧ���ɼ���Ϣ������ʽ����
			Score score=new Score(120,"��ѧ",date,90.5);
			BaseFont bfChinese=BaseFont.createFont();
			//���������������������
			Font fontChinese = new Font(bfChinese, 8,Font.BOLD);
			this.setTableText(writer, score.getId()==0?null:DataUtil.getNumKb(score.getId()),fontChinese,240+data_X, 645+data_Y);
			this.setTableText(writer, score.getScore()==0?null:DataUtil.getNumKb(score.getScore()),fontChinese,240+data_X, 630+data_Y);
		   //�ر��ĵ�
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(ParseException e){
			e.printStackTrace();
		}
	   File file=new File(filePath);
		if(file.exists()){
			return filePath;
		}else{
			return null;
		}
	}
//����һ��һ�����еı��
public void createTable(Document document) throws DocumentException{
	 PdfPTable table = new PdfPTable(3);
     //���ñ�������
     table.setTotalWidth(90);
     //����ÿһ����ռ�ĳ���
     table.setWidths(new float[]{50f, 15f, 25f});


     PdfPCell cell1 = new PdfPCell();
     Paragraph para = new Paragraph("�õ�Ԫ����");
     //���øö���Ϊ������ʾ
     para.setAlignment(1);
     cell1.setPhrase(para);
     table.addCell(cell1);

     table.addCell(new PdfPCell(new Phrase("�o��֮·IText�̳�")));
     table.addCell(new PdfPCell(new Phrase("�o��֮·IText�̳�")));

     document.add(table);
}
public static void main(String[] args) throws FileNotFoundException, DocumentException {
	PdfUtil pf=new PdfUtil();
	pf.createPdf();
}
}
