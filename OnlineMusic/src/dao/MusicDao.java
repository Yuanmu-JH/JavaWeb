package dao;

import entity.Music;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//有关音乐的数据库操作
public class MusicDao {

    //查询全部歌单
    public List<Music> findMusic() {

        List<Music> musicList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from music";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                //将组成的music对象加到表中
                musicList.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, resultSet);
        }
        return musicList;
    }

    //根据id查找音乐
    public Music findMusicById(int id) {
        Music music = new Music();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from music where id=?";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, resultSet);
        }
        return music;
    }

    //根据关键字查询歌单
    public List<Music> ifMusic(String str) {
        List<Music> musicList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            //歌名模糊查询
            String sql = "select * from music where title like '%" + str + "%'";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                //将组成的music对象加到表中
                musicList.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, resultSet);
        }
        return musicList;
    }

    /*
    上传音乐：
    ①上传文件本身给服务器
    ②将音乐信息插入至数据库中，在dao层做这一步
     */
    public int insert(String title, String singer, String time, String url, int userid) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String sql = "insert into music(title, singer, time, url, userid) values (?,?,?,?,?)";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, title);
            statement.setString(2, singer);
            statement.setString(3, time);
            statement.setString(4, url);
            statement.setInt(5, userid);
            //返回值为被影响的行数
            int ret = statement.executeUpdate();
            if (ret == 1) {
                //插入成功
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, null);
        }
        //到这里则说明插入失败
        return 0;
    }

    //根据id删除歌曲
    public int deleteMusicById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String sql = "delete from music where id=?";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            int ret = statement.executeUpdate();
            if(ret == 1){
                //返回值为1说明music表中的数据成功
                //还要判断这首歌在不在我喜欢的歌单中，如果在，删除
                if(findLoveMusicOnDel(id)){
                    //tmp判断是否删除成功
                   int tmp = removeLoveMusicOnDel(id);
                   if(tmp == 1){
                       //到这一步 歌单中删除了我喜欢也删除了 则彻底删除
                       return 1;
                   }
                }
                //这首歌不在我喜欢的歌单中
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, null);
        }
        return 0;
    }


    //查找要删除的歌曲是否在我喜欢中
    private boolean findLoveMusicOnDel(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from lovemusic where id=?";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                //找到了
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, null);
        }
        return false;
    }

    //删除要我喜欢歌单中的要删除歌曲
    private int removeLoveMusicOnDel(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        int ret = 0;
        try {
            String sql = "delete * from lovemusic where id=?";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            ret = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, null);
        }
        return ret;
    }

    //添加喜欢的音乐的时候，需要先判断该音乐是否已经被添加过了
    public boolean findMusicByMusicId(int user_id,int musicID) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from lovemusic where user_id=? and music_id=?";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1,user_id);
            statement.setInt(2,musicID);
            resultSet = statement.executeQuery();
            if (resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection,statement,null);
        }
        return false;
    }

    //添加我喜欢的音乐
    public boolean insertLoveMusic(int userId,int musicId){
        Connection connection = null;
        PreparedStatement statement = null;
        int ret = 0;
        try {
            String sql = "insert into lovemusic(user_id, music_id) values (?,?)";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1,userId);
            statement.setInt(2,musicId);
            ret = statement.executeUpdate();
            if(ret == 1){
                //插入成功
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, null);
        }
        return false;
    }

    // 移除我喜欢的音乐，要有两个变量
    // 因为同一首音乐可能多个用户喜欢，连表查询
    public int removeLoveMusic(int userId,int musicId){
        Connection connection = null;
        PreparedStatement statement = null;
        int ret = 0;
        try {
            String sql ="delete from lovemusic where user_id=? and music_id=?";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1,userId);
            statement.setInt(2,musicId);
            ret = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, null);
        }
        return ret;
    }

    //查找用户喜欢的全部歌单
    public List<Music> findLoveMusic(int user_id){
        List<Music> musicList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql ="select m.id as music_id,title,singer,time,url,userid from lovemusic lm,music m where lm.music_id=m.id and user_id=?";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1,user_id);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                Music music = new Music();
                music.setId(resultSet.getInt("music_id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musicList.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, resultSet);
        }
        return musicList;
    }

    //根据关键字查询喜欢的歌曲
    public List<Music> ifMusicLove(String str,int user_id){
        List<Music> musicList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql ="select m.id as music_id,title,singer,time,url,userid from lovemusic lm,music m where lm.music_id=m.id and user_id=? and title like '%"+str+"%'";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1,user_id);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                Music music = new Music();
                music.setId(resultSet.getInt("music_id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musicList.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getclose(connection, statement, resultSet);
        }
        return musicList;
    }
}
