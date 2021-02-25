package com.iso.logus;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import java.util.List;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public interface ApiDocumentUtils {
	static OperationRequestPreprocessor getDocumentRequest() {
		return preprocessRequest(prettyPrint());
	}
	static OperationResponsePreprocessor getDocumentResponse() {
		return preprocessResponse(prettyPrint());
	}
	
	static MultiValueMap<String, String> buildPageParameter() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		
		params.add("page", "0");
		params.add("size", "20");
		params.add("direction", "ASC");
		
		return params;
	}
	
	static List<ParameterDescriptor> pageParameter() {
		return List.of(
			parameterWithName("page").description(""),
			parameterWithName("size").description(""),
			parameterWithName("direction").description("")
				);
	}
	
	static List<FieldDescriptor> pageField() {
			return List.of(
			fieldWithPath("pageable").ignored(),
			fieldWithPath("totalElements").ignored(),
			fieldWithPath("totalPages").ignored(),
			fieldWithPath("last").ignored(),
			fieldWithPath("number").ignored(),
			fieldWithPath("size").ignored(),
			fieldWithPath("pageable").ignored(),
			fieldWithPath("sort.sorted").ignored(),
			fieldWithPath("sort.unsorted").ignored(),
			fieldWithPath("sort.empty").ignored(),
			fieldWithPath("numberOfElements").ignored(),
			fieldWithPath("first").ignored(),
			fieldWithPath("empty").ignored()
				);
	}
	
	static List<FieldDescriptor> commonErrorField() {
		return List.of(				
			fieldWithPath("[].code").description("오류 코드"),
			fieldWithPath("[].message").description("오류 메세지"));	
	}
}
