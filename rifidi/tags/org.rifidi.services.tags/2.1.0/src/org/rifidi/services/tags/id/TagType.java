/**
 * 
 */
package org.rifidi.services.tags.id;

/**
 * This is the enum for the different tag types
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public enum TagType{
//	CustomEPC96, DoD96, GID96, SGTIN96, SSCC96;

	CustomEPC96()
	{
		public byte[] getRandomTagData(String prefix)
		{
			return org.rifidi.services.tags.id.CustomEPC96.getRandomTagData(prefix);
		}
	},
	DoD96()
	{
		public byte[] getRandomTagData(String prefix)
		{
			return org.rifidi.services.tags.id.DoD96.getRandomTagData(prefix);
		}
	},
	GID96()
	{
		public byte[] getRandomTagData(String prefix)
		{
			return org.rifidi.services.tags.id.GID96.getRandomTagData(prefix);
		}
	},
	SGTIN96()
	{
		public byte[] getRandomTagData(String prefix)
		{
			return org.rifidi.services.tags.id.SGTIN96.getRandomTagData(prefix);
		}
	},
	SSCC96()
	{
		public byte[] getRandomTagData(String prefix)
		{
			return org.rifidi.services.tags.id.SSCC96.getRandomTagData(prefix);
		}
	};
	
	public abstract byte[] getRandomTagData(String prefix);
}
