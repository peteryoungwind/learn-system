package com.learnsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learnsystem.entity.AlbumEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlbumMapper extends BaseMapper<AlbumEntity> {
}
