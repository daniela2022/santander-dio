package com.project.bootcamp.services;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bootcamp.exception.BusinessException;
import com.project.bootcamp.exception.NotFoundException;
import com.project.bootcamp.mapper.StockMapper;
import com.project.bootcamp.model.Stock;
import com.project.bootcamp.model.dto.StockDto;
import com.project.bootcamp.repository.StockRepository;
import com.project.bootcamp.util.MessageUtils;

@Service
public class StockServices {
	
	@Autowired
	private StockRepository repository;
	
	@Autowired
	private StockMapper mapper;
	
    @Transactional
	public StockDto save(StockDto dto) {
    	Optional<Stock> optionalStock = repository.findByNameAndDate(dto.getName(), dto.getDate());
    	if(optionalStock.isPresent()) {
    		throw new BusinessException(MessageUtils.STOCK_ALREADY_EXISTS);
    	}
    		
    	
		Stock stock = mapper.toEntity(dto);
		repository.save(stock);
		return mapper.toDto(stock);
	}

    @Transactional
	public StockDto update(StockDto dto) {
    	Optional<Stock> optionalStock = repository.findByStockUpdate(dto.getName(), dto.getDate(), dto.getId());
    	if(optionalStock.isPresent()) {
    		throw new BusinessException(MessageUtils.STOCK_ALREADY_EXISTS);
    	}
    		
		Stock stock = mapper.toEntity(dto);
		repository.save(stock);
		return mapper.toDto(stock);
		
	}

    @Transactional(readOnly = true) 
    public List<StockDto> findAll() {
		return mapper.toDto(repository.findAll());
		
	}

	public StockDto findById(Long id) {
		return repository.findById(id).map(mapper::toDto).orElseThrow(NotFoundException::new);
	}

	public StockDto delete(Long id) {
		StockDto dto = this.findById(id);
		repository.deleteById(dto.getId());
		return dto;
	}

	 @Transactional(readOnly = true) 
	public List<StockDto> findByToday() {
		 return repository.findByToday(LocalDate.now()).map(mapper::toDto).orElseThrow(NotFoundException::new);
		 
		
	}
	
	

}
