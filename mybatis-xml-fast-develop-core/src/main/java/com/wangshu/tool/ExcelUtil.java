package com.wangshu.tool;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.util.WorkBookUtil;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExcelUtil {

    public static void writeOneSheetExcel(
            List<Map<String, Object>> dataList,
            List<String> dataKeyList,
            List<String> columnList,
            List<WriteHandler> writeHandlerList,
            List<Converter<?>> converterList,
            String fileName,
            HttpServletResponse response
    ) throws IOException {
        writeOneSheetExcel(
                dataList,
                dataKeyList,
                columnList,
                fileName.replaceAll(".xlsx", "").replaceAll(".xls", ""),
                writeHandlerList,
                converterList,
                fileName,
                response
        );
    }

    public static void writeOneSheetExcel(
            List<Map<String, Object>> dataList,
            List<String> dataKeyList,
            List<String> columnList,
            String sheet,
            List<WriteHandler> writeHandlerList,
            List<Converter<?>> converterList,
            String fileName,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        writeOneSheetExcel(
                dataList,
                dataKeyList,
                columnList,
                sheet,
                writeHandlerList,
                converterList,
                fileName,
                response.getOutputStream()
        );
    }

    public static void writeOneSheetExcel(
            List<Map<String, Object>> dataList,
            List<String> dataKeyList,
            List<String> columnList,
            String sheet,
            List<WriteHandler> writeHandlerList,
            List<Converter<?>> converterList,
            String fileName,
            OutputStream outputStream
    ) {
        List<List<Object>> newData = new ArrayList<>();
        List<List<String>> newColumn = new ArrayList<>();
        dataList.forEach(item -> {
            List<Object> list = new ArrayList<>();
            dataKeyList.forEach(key -> list.add(item.get(key)));
            newData.add(list);
        });
        columnList.forEach(item -> newColumn.add(List.of(item)));
        writeOneSheetExcel(
                newData,
                newColumn,
                sheet,
                writeHandlerList,
                converterList,
                fileName,
                outputStream
        );
    }

    public static void writeOneSheetExcel(List<List<Object>> dataList,
                                          List<List<String>> columnsList,
                                          String sheet,
                                          List<WriteHandler> writeHandlerList,
                                          List<Converter<?>> converterList,
                                          String fileName,
                                          OutputStream outputStream) {
        writeExcel(
                List.of(dataList),
                List.of(columnsList),
                List.of(sheet),
                writeHandlerList,
                converterList,
                fileName,
                outputStream
        );
    }

    public static void writeExcel(
            List<List<List<Object>>> dataList,
            List<List<List<String>>> columnList,
            List<String> sheetList,
            List<WriteHandler> writeHandlerList,
            List<Converter<?>> converterList,
            String fileName,
            OutputStream outputStream
    ) {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(outputStream).autoCloseStream(Boolean.FALSE).excelType(ExcelTypeEnum.XLSX);
        if (Objects.isNull(writeHandlerList)) {
            writeHandlerList = new ArrayList<>();
        }
        if (Objects.isNull(converterList)) {
            converterList = new ArrayList<>();
        }
        converterList.add(new TimestampConverter());
        converterList.forEach(excelWriterBuilder::registerConverter);
        try (ExcelWriter excelWriter = excelWriterBuilder.build()) {
            for (int index = 0; index < dataList.size(); index++) {
                if (Objects.nonNull(dataList.get(index)) && Objects.nonNull(columnList.get(index)) && Objects.nonNull(sheetList.get(index))) {
                    WriteSheet writeSheet = EasyExcel.writerSheet(index, sheetList.get(index)).head(columnList.get(index)).build();
                    excelWriter.write(dataList.get(index), writeSheet);
                }
            }
        }
    }

    public static WriteCellStyle getDefaultCellStyle() {
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        WriteFont cellFont = new WriteFont();
        cellFont.setFontHeightInPoints((short) 14);
        contentWriteCellStyle.setWriteFont(cellFont);
        return contentWriteCellStyle;
    }

    public static WriteCellStyle getDefaultHeaderStyle() {
        WriteCellStyle writeCellStyle = new WriteCellStyle();
        writeCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        writeCellStyle.setBorderBottom(BorderStyle.THIN);
        writeCellStyle.setBorderTop(BorderStyle.THIN);
        writeCellStyle.setBorderLeft(BorderStyle.THIN);
        writeCellStyle.setBorderRight(BorderStyle.THIN);
        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        WriteFont headerFont = new WriteFont();
        headerFont.setFontHeightInPoints((short) 16);
        headerFont.setBold(true);
        writeCellStyle.setWriteFont(headerFont);
        return writeCellStyle;
    }

    public static HorizontalCellStyleStrategy getDefaultExcelStyle() {
        return new HorizontalCellStyleStrategy(getDefaultHeaderStyle(), getDefaultCellStyle());
    }

}

class TimestampConverter implements Converter<Timestamp> {

    @Override
    public Class<Timestamp> supportJavaTypeKey() {
        return Timestamp.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Timestamp value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        WriteCellData<?> cellData = new WriteCellData<>(value);
        String format = null;
        if (Objects.nonNull(contentProperty) && Objects.nonNull(contentProperty.getDateTimeFormatProperty())) {
            format = contentProperty.getDateTimeFormatProperty().getFormat();
        }
        WorkBookUtil.fillDataFormat(cellData, format, DateUtils.defaultDateFormat);
        return cellData;
    }

}
