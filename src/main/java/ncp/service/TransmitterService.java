package ncp.service;

import ncp.model.Transmitter;
import ncp.repository.TransmitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static ncp.config.ProjectConstants.ROW_COUNT;

@Service
public class TransmitterService {
    @Autowired
    private TransmitterRepository transmitterRepository;
    @Autowired
    private TransmitterStatusService transmitterStatusService;

    public long pageCount(){
        long nPage=transmitterRepository.count()/ROW_COUNT + (transmitterRepository.count()%ROW_COUNT == 0? 0:1);
        return nPage == 0? nPage+1 : nPage;
    }

    public List<Transmitter> transmitterListByNumberPageList(long numberPageList){
        return transmitterRepository.selectByLimitOffset(ROW_COUNT, (numberPageList-1)*ROW_COUNT);
    }

    public Transmitter saveNew(String description){
        Transmitter transmitter = new Transmitter();
        transmitter.setDescription(description);
        transmitter.setStatus(transmitterStatusService.getById(1));
        transmitter.setAvailableAddresses(new HashSet<>());
        return transmitterRepository.save(transmitter);
    }

    public Transmitter save(Transmitter transmitter){
        return transmitterRepository.save(transmitter);
    }

    public Transmitter setStatus(long transmitterId, long statusId){
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setStatus(transmitterStatusService.getById(statusId));
        return transmitterRepository.save(transmitter);
    }
    public Transmitter getById(long id){
        return transmitterRepository.getById(id);
    }

    public void deleteById(long id){
        transmitterRepository.deleteById(id);
    }
}
