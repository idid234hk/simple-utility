# simple-utility

##### I am new to github. Hoping my utility can save your time.

First utility for mybatis result converting.

If you will convert mybatis domain object to your custom domain object this utility might save your time.

Example in traditional way:

	List<AbcDTO> result = Collections.emptyList();
	
	AbcExample example = new AbcExample();
	example.createCriteria().andIdEqualTo("1");    
	List<Abc> mybatisResultList = AbcMapper.selectByExample(example);	
	
	if(!mybatisResultList.isEmpty()){
		result = new ArrayList<>();
		for(Abc abc:mybatisResultList){
			result.add(dozerBeanMapper.map(abc,AbcDTO.class));
		}
	}
    return result;

Example in new way:

	AbcExample example = new AbcExample();
	example.createCriteria().andIdEqualTo("1");   
	
	return BeanListUtilsWithDozer.convertListToList(AbcMapper.selectByExample(example),AbcDTO.class);
