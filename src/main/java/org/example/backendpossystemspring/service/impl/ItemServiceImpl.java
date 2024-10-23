package org.example.backendpossystemspring.service.impl;

import org.example.backendpossystemspring.customStatusCodes.SelectedCustomerAndItemErrorStatus;
import org.example.backendpossystemspring.dao.ItemDao;
import org.example.backendpossystemspring.dto.ItemStatus;
import org.example.backendpossystemspring.dto.impl.Item;
import org.example.backendpossystemspring.entity.impl.ItemEntity;
import org.example.backendpossystemspring.exception.DataPersistException;
import org.example.backendpossystemspring.exception.ItemNotFoundException;
import org.example.backendpossystemspring.service.ItemService;
import org.example.backendpossystemspring.util.AppUtil;
import org.example.backendpossystemspring.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private Mapping mapping;
    /*private static List<NoteDTO> noteDTOList = new ArrayList<>();

    NoteServiceImpl(){
        noteDTOList.add(new NoteDTO())
    }*/
    @Override
    public void saveItem(Item itemDTO) {
        itemDTO.setItemCode(AppUtil.generateItemId());
        ItemEntity savedNote = itemDao.save(mapping.toItemEntity(itemDTO));
        if(savedNote == null){
            throw new DataPersistException("Item not saved.");
        }
    }

    @Override
    public List<Item> getAllItems() {
        return mapping.asItemDTOList(itemDao.findAll());
    }

    @Override
    public ItemStatus getItem(String itemId) {
        if(itemDao.existsById(itemId)){
            var selectedItem = itemDao.getReferenceById(itemId);
            return mapping.toItemDTO(selectedItem);
        }
        return new SelectedCustomerAndItemErrorStatus(2, "Selected item not found.");
    }

    @Override
    public void deleteItem(String itemID) {
        Optional<ItemEntity> foundNote = itemDao.findById(itemID);
        if(!foundNote.isPresent()){
            throw new ItemNotFoundException("Item not found.");
        }else{
            itemDao.deleteById(itemID);
        }
    }

    @Override
    public void updateItem(String noteId, Item itemDTO) {
        Optional<ItemEntity> findNote = itemDao.findById(noteId);
        if(!findNote.isPresent()){
            throw new ItemNotFoundException("Item not found");
        }else {
            findNote.get().setItemName(itemDTO.getItemName());
            findNote.get().setItemQuantity(itemDTO.getItemQuantity());
            findNote.get().setItemPrice(itemDTO.getItemPrice());
        }
    }
}
