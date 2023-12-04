package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.UserRequestQueryVO;
import exam.common.vo.UserResponseVO;
import exam.dao.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface UserMapper extends BaseMapper<UserEntity> {

    Set<String> getPerms(@Param("userId") Long userId);

    Page<UserResponseVO> pageList(@Param("vo") UserRequestQueryVO vo, Page page);
}
