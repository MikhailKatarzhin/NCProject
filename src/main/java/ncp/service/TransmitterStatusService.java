package ncp.service;

import ncp.model.TransmitterStatus;
import ncp.repository.TransmitterStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransmitterStatusService {
    @Autowired
    private TransmitterStatusRepository transmitterStatusRepository;
    public TransmitterStatus getByName(String name){
        return transmitterStatusRepository.getByStatusName(name);
    }
    public TransmitterStatus getById(long id){
        return transmitterStatusRepository.getById(id);
    }
    public List<TransmitterStatus> getAll(){
        return transmitterStatusRepository.findAll();
    }
}
