package com.ecommerce.app.service;

import com.ecommerce.app.dto.ProviderDTO;
import com.ecommerce.app.dto.ProviderOptionDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Provider;
import com.ecommerce.app.model.ProviderOption;
import com.ecommerce.app.repository.ProviderOptionRepo;
import com.ecommerce.app.repository.ProviderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {
    @Autowired
    ProviderRepo providerRepo;
    private ProviderOptionRepo providerOptionRepo;


    public ResponseDTO<?> getProviderByCode(String code) {
        try {
            Optional<Provider> provider = providerRepo.findByCode(code);
            if (provider.isEmpty())
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Provider Not Found", null);
            Provider providerFound = provider.get();
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setName(providerFound.getName());
            providerDTO.setCode(providerFound.getCode());
            List<ProviderOptionDTO> providerOptionDTOS = getProviderOptionDTOS(providerFound);
            providerDTO.setProviderOptions(providerOptionDTOS);
            return new ResponseDTO<>(HttpStatus.OK, "Provider Found", providerDTO);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    private static List<ProviderOptionDTO> getProviderOptionDTOS(Provider providerFound) {
        List<ProviderOptionDTO> providerOptionDTOS = new ArrayList<>();
        List<ProviderOption> providerOptions = providerFound.getProviderOptions();
        providerOptions.forEach(
                providerOption ->
                {
                    ProviderOptionDTO dto = new ProviderOptionDTO();
                    dto.setOption(providerOption.getOption());
                    dto.setValue(providerOption.getValue());
                    providerOptionDTOS.add(dto);
                }
        );
        return providerOptionDTOS;
    }

    public ResponseDTO<?> getAllProvider() {
        try {
            List<Provider> providers = providerRepo.findAll();
            if (!providers.isEmpty()) {
                List<ProviderDTO> providerDTOS = new ArrayList<>();
                providers.forEach(
                        provider -> {
                            ProviderDTO providerDTO = new ProviderDTO();
                            providerDTO.setName(provider.getName());
                            providerDTO.setCode(provider.getCode());
                            List<ProviderOptionDTO> providerOptionDTOS = getProviderOptionDTOS(provider);
                            providerDTO.setProviderOptions(providerOptionDTOS);
                            providerDTOS.add(providerDTO);
                        }
                );
                return new ResponseDTO<>(HttpStatus.OK, "Provider Found", providerDTOS);
            }
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Provider Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }

    public ResponseDTO<?> createProvider(ProviderDTO provider) {
        try {
            Provider providerFound = providerRepo.findByCode(provider.getCode()).orElse(new Provider());
            if (providerFound.getId() != null)
                return new ResponseDTO<>(HttpStatus.CONFLICT, "Provider Already Exists", null);
            providerFound.setCreatedAt(LocalDateTime.now());
            providerFound.setName(provider.getName());
            providerFound.setCode(provider.getCode());
            providerFound = providerRepo.save(providerFound);
            Provider finalProviderFound = providerFound;
            provider.getProviderOptions().forEach(
                    providerOptionDTO -> {
                        ProviderOption option = new ProviderOption();
                        option.setProvider(finalProviderFound);
                        option.setOption(providerOptionDTO.getOption());
                        option.setValue(providerOptionDTO.getValue());
                        providerOptionRepo.save(option);
                    }
            );
            provider.setId(providerFound.getId());
            return new ResponseDTO<>(HttpStatus.CREATED, "Provider Created", provider);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> updateProvider(ProviderDTO provider) {
        try {
            Provider providerFound = providerRepo.findByCode(provider.getCode()).orElse(null);
            if (providerFound == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Provider Not Found", null);

            providerFound.setUpdatedAt(LocalDateTime.now());
            providerFound.setName(provider.getName());
            List<ProviderOption> providerOptions = providerOptionRepo.findAll()
                    .stream().filter(x -> x.getProvider().getId().equals(providerFound.getId()))
                    .toList();
            List<String> options = new ArrayList<>();
            providerOptions.forEach(
                    providerOption -> options.add(providerOption.getOption())
            );
            provider.getProviderOptions().forEach(
                    providerOptionDTO -> {
                        if (!options.contains(providerOptionDTO.getOption())) {
                            ProviderOption option = new ProviderOption();
                            option.setProvider(providerFound);
                            option.setOption(providerOptionDTO.getOption());
                            option.setValue(providerOptionDTO.getValue());
                            providerOptionRepo.save(option);
                        }
                    }
            );
            providerRepo.save(providerFound);
            provider.setId(providerFound.getId());
            return new ResponseDTO<>(HttpStatus.ACCEPTED, "Provider Updated", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> deleteProvider(String code) {
        try {
            Optional<Provider> provider = providerRepo.findByCode(code);
            if (provider.isEmpty())
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Provider Not Found", null);
            providerRepo.delete(provider.get());
            return new ResponseDTO<>(HttpStatus.ACCEPTED, "Provider Deleted", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }
}
