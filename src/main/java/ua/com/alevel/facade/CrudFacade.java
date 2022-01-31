package ua.com.alevel.facade;

import ua.com.alevel.web.dto.request.RequestDto;
import ua.com.alevel.web.dto.response.ResponseDto;

public interface CrudFacade<REQ extends RequestDto, RES extends ResponseDto> {

    void create(REQ req);
    void delete(Long id);
}
