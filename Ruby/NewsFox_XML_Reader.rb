=begin
	This script takes a master.xml file (used by the NewsFox RSS extention for
	FireFox) on the command line and prints a bunch of information from it.
	
	I don't imagine this is very useful, but just in case...

	Copyright 2010 Stephen Sloan
	This code is free software that you can redistribute and/or modify under the
	terms of the GNU General Public License as published by the Free Software 
	Foundation, either version 2 of the License, or (at your option) any later 
	version.
=end

require 'rubygems'
gem 'nokogiri'
require 'Nokogiri'

WORKING_DIR = ARGV[0]
MASTER_XML_FILE_NAME = 'master.xml'
MASTER_XML_FILE = WORKING_DIR + '\\' + MASTER_XML_FILE_NAME

# Extend string to strip CDATA characters
class String
	CDATA_REGEX = Regexp.new('<!\[CDATA\[(.*)\]\]>')
	def strip_cdata
		md = CDATA_REGEX.match(self)
		md ? md[1] : self
	end
end

# This method exists in git but not in my 1.4.3.1 copy
class Nokogiri::XML::Reader
	def element?
		node_type == Nokogiri::XML::Node::ELEMENT_NODE
	end
end

# Print some stuff from the input XML file
if File.exists? MASTER_XML_FILE
	File.open MASTER_XML_FILE, 'r' do |f|
		reader = Nokogiri::XML::Reader.from_io(f)
		reader.each do |node|
		
			def get_inside(node)
				node.inner_xml.strip_cdata
			end

			uid = nil

			case node.name
			when 'url'
				puts get_inside(node)
			when 'dname'
				puts "  Name: " + get_inside(node)
			when 'home'
				puts "  Home: " + get_inside(node)
			when 'lastUpdate'
				puts "  Last Update: " + get_inside(node)
			when 'uid'
				uid = node.inner_xml
			end if node.element?

			if uid
				child_xml_file_name = WORKING_DIR + '\\' +
				                      node.inner_xml + '.xml'
				if File.exists? child_xml_file_name
					File.open child_xml_file_name, 'r' do |cf|
						puts "  Posts..."					
						child_reader = Nokogiri::XML::Reader.from_io(cf)
						child_reader.each do |child_node|

							case child_node.name
							when 'title'
								puts ' ' * 4 + 'Post: ' + get_inside(child_node)[0..60]
							end if child_node.element?

						end
					end
				end
			end

		end
	end
else
	puts "That file does not exist"
end